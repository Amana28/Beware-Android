package com.example.beware.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Beware : Screen("beware")
    object Profile : Screen("profile")
    object Report : Screen("report")
}