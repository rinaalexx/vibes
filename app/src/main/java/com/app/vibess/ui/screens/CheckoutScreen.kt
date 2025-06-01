package com.app.vibess.ui.screens

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.vibess.data.model.Order
import com.app.vibess.data.model.OrderItem
import com.app.vibess.data.model.Shipping
import com.app.vibess.data.model.CartItem
import com.app.vibess.functions.getCartItemsByUserId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CheckoutScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid?.hashCode() ?: 0
    var isLoading by remember { mutableStateOf(true) }
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

    // Состояния для данных пользователя
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var shippingMethod by remember { mutableStateOf("Courier") }
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var totalAmount by remember { mutableStateOf(0.0) }

    // Получаем товары из корзины
    LaunchedEffect(userId) {
        getCartItemsByUserId(userId, onSuccess = { items ->
            cartItems = items
            calculateTotalAmount(cartItems) { total ->
                totalAmount = total
            }
        }, onFailure = { error ->
            Log.e("Cart", "Failed to load cart items: $error")
        })
    }

    // Сохранение данных заказа в Firestore
    fun createOrder() {
        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            val orderId = db.collection("orders").document().id // Генерация строкового ID заказа

            // Суммируем стоимость всех товаров в корзине
            val totalPrice = totalAmount

            // Создаем заказ
            val order = Order(
                orderId = orderId,  // Используем строковый ID
                userId = currentUser.uid,
                totalPrice = totalPrice,
                shippingMethod = shippingMethod,
                shippingAddress = address,
                firstName = firstName,
                lastName = lastName
            )

            // Сохраняем заказ в Firestore
            db.collection("orders")
                .document(orderId)
                .set(order)
                .addOnSuccessListener {
                    // После создания заказа, сохраняем товары в "Order_Items"
                    val orderItem = OrderItem(
                        orderId = orderId,  // Используем orderId
                        items = cartItems // Передаем все товары из корзины
                    )
                    db.collection("order_items").add(orderItem)

                    // Создаем информацию о доставке
                    val shipping = Shipping(
                        orderId = orderId,
                        shippingMethod = shippingMethod,
                        shippingAddress = address
                    )

                    db.collection("shipping").add(shipping)

                    // Перенаправляем на страницу подтверждения
                    navController.navigate("order_confirmed")
                }
                .addOnFailureListener { e ->
                    Log.e("Order", "Error creating order: ${e.message}")
                }
        }
    }

    // UI для ввода данных
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Оформление заказа", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Поля для ввода данных
        OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Адрес доставки") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = shippingMethod == "Courier", onClick = { shippingMethod = "Courier" })
            Text("Курьер")

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(selected = shippingMethod == "Pickup", onClick = { shippingMethod = "Pickup" })
            Text("Самовывоз")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Общая сумма
        Text(
            text = "Total: $totalAmount P",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка оформления заказа
        Button(onClick = { createOrder() }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
            Text(text = "Подтвердить заказ", color = Color.White)
        }
    }
}
