package com.app.vibess.ui.screens
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.vibess.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CategoryDetailScreen(category: String, navController: NavController) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Animatable for scaling the loading image
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            scale.animateTo(
                targetValue = 1.5f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1000
                        1.5f at 500
                        1f at 1000
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
        when {
            isLoading -> {
                // Show animated PNG
                Image(
                    painter = rememberAsyncImagePainter("https://res.cloudinary.com/dxdspcnk6/image/upload/v1748096212/vibes_ylg4jr.png"), // replace with your PNG image path
                    contentDescription = "Loading...",
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
            }
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
                        ProductCard(product = product, onClick = {
                            navController.navigate("product/${product.sku}")
                        })
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
            .clickable {
                Log.d("ProductCard", "Product clicked: ${product.name}")
                onClick() },
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
                    .height(180.dp),
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
