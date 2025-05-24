package com.app.vibess.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.vibess.data.model.Product
import com.app.vibess.functions.findProductById

@Composable
fun ProductDetailScreen(productSku: Int) {
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(productSku) {
        // Загружаем продукт по SKU
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
            Text("Название: ${product!!.name}")
            Text("Цена: ${product!!.price} руб.")
            // Можно добавить другие детали продукта
        }
        else -> Text("Продукт не найден")
    }
}
