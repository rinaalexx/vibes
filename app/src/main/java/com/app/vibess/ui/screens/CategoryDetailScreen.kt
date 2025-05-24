package com.app.vibess.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.vibess.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CategoryDetailScreen(category: String, navController: NavController) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(category) {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .whereEqualTo("category", category)
            .get()
            .addOnSuccessListener { result ->
                val productList = result.documents.mapNotNull { doc ->
                    doc.toObject(Product::class.java)
                }
                products = productList
                isLoading = false
            }
            .addOnFailureListener { exception ->
                errorMessage = exception.localizedMessage
                isLoading = false
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text(
            text = "Товары: $category",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        when {
            isLoading -> Text("Загрузка товаров...")
            errorMessage != null -> Text("Ошибка: $errorMessage")
            products.isEmpty() -> Text("Нет товаров в категории $category")
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(products) { product ->
                        ProductCard(product = product) {
                            // При нажатии можно добавить переход в детали
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Картинка
            Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Цена и скидка - предположим скидка 50%
            val discountedPrice = product.price / 2

            Text(
                text = "${product.price} р.",
                style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${discountedPrice.toInt()} р.",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.error)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = product.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
