package com.app.vibess

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.app.vibess.ui.components.BottomBar
import com.app.vibess.navigation.AppNavHost
import com.app.vibess.ui.components.LogoHeader
import com.app.vibess.ui.theme.VibesTheme
import com.cloudinary.android.MediaManager
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay


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
        /*  val db = FirebaseFirestore.getInstance()
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
        */

        setContent {
            VibesTheme {
                val navController = rememberNavController()
                LaunchedEffect(Unit) {
                    delay(3000) // 3 секунды паузы
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true } // Удаляем splash из стека
                    }
                }

 // Scaffold для добавления BottomBar и LogoHeader
                Scaffold(
                    bottomBar = { BottomBar(navController = navController) } // BottomBar в нижней части экрана
                ) { innerPadding -> // padding для контента
                    Column(modifier = Modifier.padding(innerPadding)  // вот здесь используем padding
                        .fillMaxSize()) {
                        LogoHeader() // Логотип сверху
                        AppNavHost(navController = navController)  // Навигация
                    }
                }
            }
        }
    }
}
