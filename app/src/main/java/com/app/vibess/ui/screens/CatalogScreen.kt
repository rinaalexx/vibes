package com.app.vibess.ui.screens

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.navigation.compose.rememberNavController

@Composable
fun CatalogScreen(navController: NavController) {
    // Список категорий для отображения
    val categories = listOf("Футболки", "Худи")

    // Главная колонка с категориями
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Каталог",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Отображение каждой категории
        categories.forEach { category ->
            CategoryItem(category = category, navController = navController)
        }
    }
}

@Composable
fun CategoryItem(category: String, navController: NavController) {
    // Количество пространства между категориями
    Spacer(modifier = Modifier.height(16.dp))

    // Стиль для карточки категории
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable {
                // Навигация по категориям
                navController.navigate("catalog/$category")
            },
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = category,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview() {
    CatalogScreen(navController = rememberNavController())
}
