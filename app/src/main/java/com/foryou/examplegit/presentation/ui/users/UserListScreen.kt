package com.foryou.examplegit.presentation.ui.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.foryou.examplegit.R
import com.foryou.examplegit.datasource.local.toDomain
import com.foryou.examplegit.presentation.component.CircularIndeterminateProgressBar
import com.foryou.examplegit.presentation.component.ErrorLayout
import com.foryou.examplegit.presentation.ui.navigation.NavigationScreen
import com.foryou.examplegit.presentation.viewmodel.UserViewModel
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(modifier: Modifier, navController: NavController) {
    val viewModel: UserViewModel = hiltViewModel()
    val users = viewModel.users.collectAsLazyPagingItems()

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when (users.loadState.refresh) { // Initial loading
            is LoadState.Loading -> {
                // Data is loading for first time
                CircularIndeterminateProgressBar(isDisplayed = true, 0.4f)
            }

            is LoadState.NotLoading -> {
                PullToRefreshBox(
                    isRefreshing = isRefreshing, onRefresh = {
                        isRefreshing = true
                        users.refresh()
                    }, modifier = modifier, state = pullRefreshState
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(users.itemCount) { index ->
                            users[index]?.let { user ->
                                UserItem(user.toDomain()) {
                                    navController.navigate(
                                        NavigationScreen.UserDetailScreen.route.plus(
                                            "/${user.login}"
                                        )
                                    )
                                }
                            }
                        }

                        // Loading More Items (Pagination)
                        when (users.loadState.append) {
                            is LoadState.Loading -> {
                                Timber.e("Loading More Items LoadState.Loading")
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .size(32.dp)
                                        )

                                        Text(text = stringResource(R.string.load_more))
                                    }
                                }
                            }

                            is LoadState.Error -> {
                                Timber.e("Loading More Items LoadState.Error")
                                item {
                                    Text(
                                        text = "Error loading more users",
                                        color = Color.Red,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .clickable { users.retry() },
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                }

                isRefreshing = false
            }

            is LoadState.Error -> {
                ErrorLayout(
                    stringResource(R.string.error_loading),
                    stringResource(R.string.error_loading_btn)
                ) {
                    users.retry()
                }
            }
        }
    }
}

@Preview
@Composable
fun UserListScreenPreview() {
    UserListScreen(Modifier.fillMaxSize(), rememberNavController())
}