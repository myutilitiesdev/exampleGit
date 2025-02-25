package com.foryou.examplegit.presentation.ui.navigation

sealed class NavigationScreen(
    val route: String,
    val objectName: String = "",
    val objectPath: String = ""
) {

    data object HomeScreen :
        NavigationScreen(route = "home_screen")

    data object UserDetailScreen : NavigationScreen(
        "user_detail_screen", objectName = "username", objectPath = "/{username}"
    )
}