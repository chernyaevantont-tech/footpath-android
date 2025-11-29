package com.example.footpath.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")
    object Map : Screen("map_screen")
    object Friends : Screen("friends_screen")
    object Paths : Screen("paths_screen")
    object Profile : Screen("profile_screen")
    object MyPlaces : Screen("my_places_screen")
    object Moderation : Screen("moderation_screen")
}