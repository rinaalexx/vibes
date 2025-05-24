package com.app.vibess.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.material3.Text
import com.app.vibess.ui.components.SplashScreen
import com.app.vibess.ui.screens.*
import com.app.vibess.functions.findProductById
import com.app.vibess.data.model.Product

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("splash") {
            SplashScreen()
        }

        composable("home") {
            HomeScreen(
                onShopNowClick = { navController.navigate("catalog") },
                onCreateNowClick = { navController.navigate("customization/1") },
                onCategoryClick = { categoryName ->
                    navController.navigate("catalog?category=${categoryName.lowercase()}")
                }
            )
        }

        composable("catalog") {
            CatalogScreen(navController = navController)
        }

        composable("catalog/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            CategoryDetailScreen(category = category ?: "Unknown", navController = navController)
        }

        composable(
            route = "product/{productSku}",
            arguments = listOf(navArgument("productSku") { type = NavType.IntType })
        ) { backStackEntry ->
            val productSku = backStackEntry.arguments?.getInt("productSku") ?: 0
            ProductDetailScreen(productSku = productSku)
        }


        composable(
            "customization/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            CustomizationScreen(
                productId = backStackEntry.arguments!!.getString("productId")!!
            )
        }
    }
}
