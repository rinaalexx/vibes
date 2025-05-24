package com.app.vibess.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.app.vibess.ui.components.LogoHeader
import androidx.navigation.NavController

@Composable
fun CatalogScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {


        // Отображаем категории с картинками
        CategoryItem(
            categoryName = "Футболки",
            routeName = "tshirt",
            imageUrl = "https://res.cloudinary.com/dxdspcnk6/image/upload/v1748121625/tshirt_ytaddx.png", // Ваш URL для футболок
            navController = navController
        )
        Spacer(modifier = Modifier.height(20.dp))

        CategoryItem(
            categoryName = "Худи",
            routeName = "hoodie",
            imageUrl = "https://res.cloudinary.com/dxdspcnk6/image/upload/v1748121624/hoodie_ixrgfr.png", // Ваш URL для худи
            navController = navController
        )
    }
}

@Composable
fun CategoryItem(
    categoryName: String,
    routeName: String,
    imageUrl: String,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clickable { navController.navigate("catalog/$routeName") },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF)),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Вставляем изображение с Cloudinary
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = categoryName,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )


        }
    }
}
