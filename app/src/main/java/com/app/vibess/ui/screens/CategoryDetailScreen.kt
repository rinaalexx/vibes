package com.app.vibess.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun CategoryDetailScreen(category: String, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Товары: $category",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Заглушка для товаров (можно заменить на реальные данные)
        Text("Здесь будут товары для категории $category")
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryDetailScreenPreview() {
    CategoryDetailScreen(category = "Футболки", navController = rememberNavController())
}
