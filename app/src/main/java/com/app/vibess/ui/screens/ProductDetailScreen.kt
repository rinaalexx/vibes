package com.app.vibess.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.app.vibess.data.model.Product
import com.app.vibess.functions.findProductById

@Composable
fun ProductDetailScreen(productSku: Int) {
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Загружаем продукт по SKU
    LaunchedEffect(productSku) {
        findProductById(
            sku = productSku,
            onSuccess = { foundProduct ->
                product = foundProduct
                isLoading = false
            },
            onFailure = { e ->
                error = e.message
                isLoading = false
            }
        )
    }

    // Отображаем соответствующий UI
    when {
        isLoading -> Text("Загрузка...")
        error != null -> Text("Ошибка: $error")
        product != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Изображение товара
                Image(
                    painter = rememberAsyncImagePainter(product!!.image),
                    contentDescription = product!!.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp) // увеличиваем размер фото
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )

                // Название товара
                Text(
                    text = product!!.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Категория товара
                Text(
                    text = product!!.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Цена товара
                Text(
                    text = "${product!!.price} P",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Кнопка "Добавить в корзину"
                Button(
                    onClick = { /* Логика добавления в корзину */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp), // Круглая кнопка
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "ADD TO CART",
                        color = Color.White,

                    )
                }
            }
        }
        else -> Text("Продукт не найден")
    }
}
