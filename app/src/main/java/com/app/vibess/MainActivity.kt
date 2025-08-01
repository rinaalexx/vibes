package com.app.vibess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.vibess.ui.theme.VibesTheme
import com.app.vibess.ui.screens.CatalogScreen
import com.app.vibess.ui.screens.CustomizationScreen
import com.app.vibess.ui.screens.HomeScreen
import com.app.vibess.ui.components.BottomBar
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.app.vibess.ui.screens.ProductDetailScreen
import com.app.vibess.ui.screens.findProductById


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VibesTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)

                    ) {
                        composable("home") {
                            HomeScreen(
                                onShopNowClick = { navController.navigate("catalog") },
                                onCreateNowClick = { navController.navigate("customization/1") },
                                onCategoryClick = { categoryName ->
                                    navController.navigate("catalog?category=${categoryName.lowercase()}")
                                }
                            )
                        }

                        composable(
                            route = "catalog?category={category}",
                            arguments = listOf(navArgument("category") {
                                type = NavType.StringType
                                defaultValue = "all"
                                nullable = true
                            })
                        ) { backStackEntry ->
                            val category = backStackEntry.arguments?.getString("category") ?: "all"
                            CatalogScreen(navController = navController, categoryFilter = category)
                        }

                        composable(
                            "product/{productId}",
                            arguments = listOf(navArgument("productId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val productId = backStackEntry.arguments!!.getString("productId")!!
                            val product = findProductById(productId) // Реализуй поиск продукта в репозитории или списке
                            ProductDetailScreen(product = product, onAddToCartClick = {
                                // Обработка добавления в корзину
                            })
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
                    }
                }
            }
        }

