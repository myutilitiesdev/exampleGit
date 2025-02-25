package com.foryou.examplegit.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBefore("/")
}

fun <T : Any> LazyPagingItems<T>.pagingLoadingState(
    isLoaded: (pagingState: Boolean) -> Unit,
) {
    this.apply {
        when {
            // data is loading for first time
            loadState.refresh is LoadState.Loading -> {
                isLoaded(true)
            }
            // data is loading for second time or pagination
            loadState.append is LoadState.Loading -> {
                isLoaded(true)
            }

            loadState.refresh is LoadState.NotLoading -> {
                isLoaded(false)
            }

            loadState.append is LoadState.NotLoading -> {
                isLoaded(false)
            }
        }
    }
}