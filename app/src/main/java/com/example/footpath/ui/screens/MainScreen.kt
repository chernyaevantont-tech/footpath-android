package com.example.footpath.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.footpath.map.Role
import com.example.footpath.ui.navigation.Screen
import com.example.footpath.ui.viewmodels.MainViewModel

sealed class BottomNavItem(val screen: Screen, val icon: ImageVector, val title: String) {
    object Map : BottomNavItem(Screen.Map, Icons.Default.Map, "Карта")
    object Friends : BottomNavItem(Screen.Friends, Icons.Default.People, "Друзья")
    object Paths : BottomNavItem(Screen.Paths, Icons.Default.List, "Пути")
    object MyPlaces : BottomNavItem(Screen.MyPlaces, Icons.Default.Place, "Мои места")
    object Moderation : BottomNavItem(Screen.Moderation, Icons.Default.RateReview, "Модерация")
    object Profile : BottomNavItem(Screen.Profile, Icons.Default.AccountCircle, "Профиль")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainViewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by mainViewModel.uiState.collectAsState()

    val allItems = listOf(
        BottomNavItem.Map,
        BottomNavItem.Friends,
        BottomNavItem.Paths,
        BottomNavItem.MyPlaces,
        BottomNavItem.Profile
    )

    val moderatorItems = allItems + BottomNavItem.Moderation

    val items = when (uiState.role) {
        Role.ADMIN, Role.MODERATOR -> moderatorItems
        else -> allItems
    }

    Scaffold (
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Map.route, // Начинаем с карты
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Map.route) { MapScreen() }
            composable(Screen.Friends.route) { FriendsScreen() }
            composable(Screen.Paths.route) { PathsScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.MyPlaces.route) { MyPlacesScreen() }
            composable(Screen.Moderation.route) { ModerationScreen() }
        }
    }
}