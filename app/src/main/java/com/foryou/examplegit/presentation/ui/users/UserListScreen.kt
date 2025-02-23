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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.foryou.examplegit.presentation.component.CircularIndeterminateProgressBar
import com.foryou.examplegit.presentation.ui.navigation.NavigationScreen
import com.foryou.examplegit.presentation.viewmodel.UserViewModel
import timber.log.Timber

@Composable
fun UserListScreen(modifier: Modifier, navController: NavController) {
    val viewModel: UserViewModel = hiltViewModel()
    val users = viewModel.users.collectAsLazyPagingItems()

    Timber.d("UserListScreen")

    Box(modifier = Modifier.fillMaxSize()) {
        when (users.loadState.refresh) { // Initial loading
            is LoadState.Loading -> {
                // data is loading for first time
                CircularIndeterminateProgressBar(isDisplayed = true, 0.4f)
            }

            is LoadState.NotLoading -> {
                LazyColumn(modifier) {
                    items(users.itemCount) { index ->
                        users[index]?.let { user ->
                            UserItem(user) {
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
                            Timber.e("LoadState.Loading")
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
                            Timber.e("LoadState.Error")
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

            is LoadState.Error -> {

            }
        }
    }

//    CircularIndeterminateProgressBar(isDisplayed = isLoading.value, 0.4f)
}

@Preview
@Composable
fun UserListScreenPreview() {
    UserListScreen(Modifier.fillMaxSize(), rememberNavController())
}