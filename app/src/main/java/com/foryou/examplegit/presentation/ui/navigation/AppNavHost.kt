package com.foryou.examplegit.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.foryou.examplegit.R
import com.foryou.examplegit.presentation.ui.UserListScreen
import com.foryou.examplegit.presentation.ui.details.UserDetailScreen

@Composable
fun AppNavHost(
    modifier: Modifier,
    navController: NavHostController,
    onTitleChanged: (String) -> Unit
) {
    NavHost(
        navController, startDestination = NavigationScreen.HomeScreen.route
    ) {
        composable(NavigationScreen.HomeScreen.route) {
            onTitleChanged(stringResource(R.string.user_screen))
            UserListScreen(navController, modifier)
        }

        composable(
            NavigationScreen.UserDetailScreen.route.plus(NavigationScreen.UserDetailScreen.objectPath),
            arguments = listOf(navArgument(NavigationScreen.UserDetailScreen.objectName) {
                type = NavType.StringType
            })
        ) {
            onTitleChanged(stringResource(R.string.detail_user_screen))
            val username =
                it.arguments?.getString(NavigationScreen.UserDetailScreen.objectName) ?: ""
            UserDetailScreen(modifier, username)
        }
    }
}