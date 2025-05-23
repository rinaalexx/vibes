package com.app.vibess.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.vibess.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ViewList   // для каталога
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Home")
    object Catalog : BottomNavItem("catalog", Icons.AutoMirrored.Filled.ViewList, "Catalog")
    object Cart : BottomNavItem("cart", Icons.Filled.ShoppingCart, "Cart")
    object Profile : BottomNavItem("profile", Icons.Filled.Person, "Profile")
}

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Catalog,
        BottomNavItem.Cart,
        BottomNavItem.Profile
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    // Добавьте правильный маршрут для каталога
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Управление стеком навигации
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = false // Убедитесь, что не удаляем стартовый экран
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                }
            )
        }
    }
}
