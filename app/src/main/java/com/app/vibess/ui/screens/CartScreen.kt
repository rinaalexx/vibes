package com.app.vibess.ui.screens

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.app.vibess.data.model.CartItem
import com.app.vibess.data.model.Product
import com.app.vibess.functions.AuthViewModelCart
import com.app.vibess.functions.findProductByIdInCart
import com.app.vibess.functions.getProductPrice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CartScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var totalAmount by remember { mutableStateOf(0.0) }
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
    // Загружаем товары из корзины
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("cart_items")
                .whereEqualTo("cartId", currentUser.uid.hashCode())
                .get()
                .addOnSuccessListener { result ->
                    cartItems = result.documents.map { document ->
                        document.toObject(CartItem::class.java)!!
                    }
                    // Пересчитываем общую сумму
                    calculateTotalAmount(cartItems) { total ->
                        totalAmount = total
                    }
                    isLoading = false
                }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Корзина", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Показываем индикатор загрузки, если данные ещё не загружены
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Список товаров в корзине
            LazyColumn {
                items(cartItems) { item ->
                    CartItemRow(item, onRemove = {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        if (currentUser != null) {
                            AuthViewModelCart().removeFromCart(
                                cartId = currentUser.uid.hashCode(),
                                productSku = item.productSku,
                                onSuccess = {
                                    // После удаления обновляем список товаров
                                    cartItems =
                                        cartItems.filterNot { it.productSku == item.productSku }
                                    // Пересчитываем общую сумму
                                    calculateTotalAmount(cartItems) { total ->
                                        totalAmount = total
                                    }
                                },
                                onFailure = { errorMessage ->
                                    Log.e("Cart", "Failed to remove item: $errorMessage")
                                }
                            )

                        }
                    })
                }
            }
        }


        // Общая сумма
        Text("Total: $totalAmount P", modifier = Modifier.padding(top = 16.dp))

        // Кнопка заказа
        Box(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = {
                    navController.navigate(
                        "checkout_screen?totalAmount=$totalAmount&cartItems=${
                            cartItems.joinToString(",")
                        }"
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Прикрепляем кнопку к низу экрана
                    .fillMaxWidth() // Занимает всю ширину экрана
                    .padding(3.dp)
                    .height(48.dp), // Увеличиваем размер кнопки
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = "Заказать",
                    color = Color.White, // Белый текст
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


fun calculateTotalAmount(cartItems: List<CartItem>, onAmountCalculated: (Double) -> Unit) {
    var total = 0.0
    cartItems.forEach { item ->
        getProductPrice(item.productSku, onSuccess = { price ->
            total += price * item.quantity
            onAmountCalculated(total)
        }, onFailure = { error ->
            Log.e("Cart", "Failed to calculate total: $error")
        })
    }
}

@Composable
fun CartItemRow(item: CartItem, onRemove: () -> Unit) {
    var product by remember { mutableStateOf<Product?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    // Загружаем продукт по SKU
    LaunchedEffect(item.productSku) {
        findProductByIdInCart(
            sku = item.productSku,
            onSuccess = { foundProduct ->
                product = foundProduct
            },
            onFailure = { e ->
                error = e.message
            }
        )
    }

    // Если продукт найден, отображаем его
    if (product != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Изображение товара
            Image(
                painter = rememberAsyncImagePainter(product!!.image),
                contentDescription = product!!.name,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            // Информация о товаре
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = product!!.name, fontWeight = FontWeight.Bold)
                Text(text = "${item.quantity} x ${product!!.price} P") // Цена из продукта
            }

            // Кнопка удаления
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить товар"
                )
            }
        }
    } else {
        // Если не найден продукт, выводим ошибку
        Text("Ошибка загрузки товара: $error")
    }
}
