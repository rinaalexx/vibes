package com.app.vibess

import android.os.Bundle
import android.util.Log
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
import com.app.vibess.data.model.Product
import com.app.vibess.functions.addProductToFirestore
import com.app.vibess.ui.screens.CategoryDetailScreen
import com.app.vibess.ui.screens.ProductDetailScreen
import com.app.vibess.ui.screens.findProductById
import com.cloudinary.android.MediaManager
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                val config = mapOf(
                    "cloud_name" to "dxdspcnk6",
                    "api_key" to "531416985942647",
                    "api_secret" to "Ls_GdFqHyraZq0UmEoq0AmR8PjQ"
                )
                MediaManager.init(this, config)



        // Получаем ссылку на Firestore
        val db = FirebaseFirestore.getInstance()
        val newProduct = Product(
            name = "Футболка",
            category = "tshirt",
            color = "white",
            image = "https://res.cloudinary.com/dxdspcnk6/image/upload/v1748088894/васильевич_aqjpxc.png",
            price = 1990.0,
            sku = 1,
            stock = 100
        )

        addProductToFirestore(newProduct,
            onSuccess = { println("Продукт добавлен") },
            onFailure = { e -> println("Ошибка: ${e.message}") }
        )

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

                                        composable("catalog") {
                                            CatalogScreen(navController = navController)
                                        }
                                        composable("catalog/{category}") { backStackEntry ->
                                            val category = backStackEntry.arguments?.getString("category")
                                            CategoryDetailScreen(category = category ?: "Unknown", navController = navController)
                                        }




                        /* composable("catalog/{category}") { backStackEntry ->
                             val category = backStackEntry.arguments?.getString("category")
                             CatalogScreen(navController = navController, category = category ?: "all")
                         }
 */




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

