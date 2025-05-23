package com.app.vibess.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import com.app.vibess.data.model.Product


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    navController: NavHostController,
    categoryFilter: String = ""
) {
    val allProducts = listOf(
        Product("1", "Кружка Vibes", 499.0, category = "all"),
        Product("2", "Футболка Vibes", 1299.0, category = "tshirts"),
        Product("3", "Коврик Vibes", 799.0, category = "all"),
        Product("4", "Худи Vibes", 1599.0, category = "hoodie")
    )

    val filteredProducts = if (categoryFilter.isEmpty() || categoryFilter == "all") {
        allProducts
    } else {
        allProducts.filter { it.category == categoryFilter }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(filteredProducts) { product ->
            ProductCard(product)
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Text("${product.price} ₽", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
