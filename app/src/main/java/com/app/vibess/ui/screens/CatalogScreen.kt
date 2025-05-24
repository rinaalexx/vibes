package com.app.vibess.ui.screens

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
import androidx.navigation.NavController

@Composable
fun CatalogScreen(navController: NavController) {
    // Категории с названиями на русском и ключами для навигации
    val categories = listOf(
        "Футболки" to "tshirt",
        "Худи" to "hoodie"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Каталог",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        categories.forEach { (displayName, routeName) ->
            CategoryItem(
                categoryName = displayName,
                routeName = routeName,
                navController = navController
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CategoryItem(categoryName: String, routeName: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { navController.navigate("catalog/$routeName") },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFEFEF)),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = categoryName,
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
