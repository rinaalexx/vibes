package com.app.vibess.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.vibess.data.model.Product

@Composable
fun ProductDetailScreen(
    product: Product,
    onAddToCartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Горизонтальный слайдер изображений (пока просто 1)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Для примера, используем один URL, но можно расширить
            items(listOf(product.imageUrl)) { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(300.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            text = product.category.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "${product.price} ₽",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f)) // Чтобы кнопка была внизу

        Button(
            onClick = onAddToCartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = "ADD TO CART")
        }
    }

}
fun findProductById(productId: String): Product {
    val products = listOf(
        Product("1", "Кружка Vibes", 499.0, category = "all"),
        Product("2", "Футболка Vibes", 1299.0, category = "tshirts"),
        Product("3", "Коврик Vibes", 799.0, category = "all"),
        Product("4", "Худи Vibes", 1599.0, category = "hoodie")
    )
    return products.firstOrNull { it.id == productId }
        ?: throw IllegalArgumentException("Product with id $productId not found")
}

