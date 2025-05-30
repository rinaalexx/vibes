package com.app.vibess.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.app.vibess.ui.screens.*

@Composable
fun AppNavHost(navController: NavHostController,authViewModel: AuthViewModel) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {


        composable("home") {
            HomeScreen(
                onShopNowClick = { navController.navigate("catalog") },
                onCreateNowClick = { navController.navigate("customization") },
                onCategoryClick = { categoryName ->
                    navController.navigate("catalog?category=${categoryName.lowercase()}")
                }
            )
        }

        composable("catalog") {
            CatalogScreen(navController = navController)
        }
        composable("profile") {
          ProfileScreen(navController = navController)
        }

        composable("catalog/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            CategoryDetailScreen(category = category ?: "Unknown", navController = navController)
        }
        composable("login"){
            LoginPage(navController,authViewModel)
        }
        composable("signup"){
            SignupPage(navController,authViewModel)
        }


        composable(
            route = "product/{productSku}",
            arguments = listOf(navArgument("productSku") { type = NavType.IntType })
        ) { backStackEntry ->
            val productSku = backStackEntry.arguments?.getInt("productSku") ?: 0
            ProductDetailScreen(productSku = productSku, navController = navController)
        }


        composable("customization")
        {
            CustomizationScreen(navController = navController)
        }
    }
}
