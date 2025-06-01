package com.app.vibess.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.app.vibess.data.model.Product
import com.app.vibess.functions.AuthViewModelCart
import com.app.vibess.functions.findProductById
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProductDetailScreen(productSku: Int, navController: NavController) {
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isAddedToCart by remember { mutableStateOf(false) }  // Состояние добавления в корзину
    var error by remember { mutableStateOf<String?>(null) }
    var quantity by remember { mutableStateOf(1) }  // Количество товара в корзине
    val checkAuthStatus = remember { mutableStateOf(false) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    checkAuthStatus.value = currentUser != null

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

                if (!isAddedToCart) {
                    // Кнопка "Добавить в корзину"
                    Button(
                        onClick = {
                            // Если пользователь не авторизован, переходим на экран LoginPage
                            if (!checkAuthStatus.value) {
                                navController.navigate("login") // Переход на экран входа
                            } else {
                                val customizationId: Int? =
                                    null // Если нет кастомизации, то это null
                                // Вызываем метод добавления товара в корзину
                                val authViewModel = AuthViewModelCart()
                                authViewModel.addToCart(
                                    cartId = currentUser!!.uid.hashCode(), // cartId можно использовать как UID пользователя
                                    productSku = product!!.sku,
                                    quantity = quantity,  // Например, добавляем 1 товар
                                    customizationId = customizationId
                                )
                                isAddedToCart = true // Отметим, что товар добавлен в корзину
                            }
                        },
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
                            color = Color.White
                        )
                    }
                 } else{
                // Кнопка "В корзине" с плюсиком и минусиком
                    // Кнопка "В корзине" с плюсиком и минусиком
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Контейнер с кнопками "+" и "-"
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Кнопка "-"
                            Button(
                                onClick = {
                                    if (quantity > 1) {
                                        quantity -= 1
                                        val authViewModel = AuthViewModelCart()
                                        authViewModel.updateCartItemQuantity(
                                            cartId = currentUser!!.uid.hashCode(),
                                            productSku = product!!.sku,
                                            newQuantity = quantity
                                        )
                                    } else {
                                        // Удаляем товар, если количество стало 1
                                        val authViewModel = AuthViewModelCart()
                                        authViewModel.removeFromCart(
                                            cartId = currentUser!!.uid.hashCode(),
                                            productSku = product!!.sku
                                        )
                                        isAddedToCart = false
                                    }
                                },
                                modifier = Modifier.padding(end = 16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "-",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Количество товара
                            Text(
                                text = "$quantity",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            // Кнопка "+"
                            Button(
                                onClick = {
                                    quantity += 1
                                    val authViewModel = AuthViewModelCart()
                                    authViewModel.updateCartItemQuantity(
                                        cartId = currentUser!!.uid.hashCode(),
                                        productSku = product!!.sku,
                                        newQuantity = quantity
                                    )
                                },
                                modifier = Modifier.padding(start = 16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "+",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Текст "В корзине"
                        Text(
                            text = "В корзине",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
        else -> Text("Продукт не найден")
    }
}