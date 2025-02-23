package com.foryou.examplegit.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.foryou.examplegit.presentation.ui.navigation.NavigationScreen
import com.foryou.examplegit.presentation.viewmodel.UserViewModel

@Composable
fun UserListScreen(navController: NavController, modifier: Modifier) {
    val viewModel: UserViewModel = hiltViewModel()
    val users = viewModel.users.collectAsLazyPagingItems()

    LazyColumn(modifier) {
        items(users.itemCount) { index ->
            users[index]?.let { user ->
                UserItem(user) {
                    navController.navigate(NavigationScreen.UserDetailScreen.route.plus("/${user.login}"))
                }
            }
        }

        when (users.loadState.append) {
            is LoadState.Loading -> {
                item { CircularProgressIndicator(modifier = Modifier.fillMaxWidth()) }
            }

            is LoadState.Error -> {
                item { Text("Error loading more users", color = Color.Red) }
            }

            else -> {}
        }
    }
}