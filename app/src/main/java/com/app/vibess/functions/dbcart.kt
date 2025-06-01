package com.app.vibess.functions

import android.util.Log
import androidx.lifecycle.ViewModel
import com.app.vibess.data.model.CartItem
import com.app.vibess.data.model.Customization
import com.app.vibess.data.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.math.BigDecimal

class AuthViewModelCart : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun addToCart(cartId: Int, productSku: Int?, quantity: Int, customizationId: Int?) {
        val db = FirebaseFirestore.getInstance()

        // Проверяем, если продукт без кастомизации
        if (productSku != null) {
            // Если кастомизации нет, передаем null для customizationId
            val cartItem = if (customizationId == null) {
                // Простой продукт без кастомизации
                CartItem(cartId, productSku, quantity, "")
            } else {
                // Продукт с кастомизацией
                val f=2*2
            }

            db.collection("cart_items")
                .add(cartItem)
                .addOnSuccessListener {
                    // Успешное добавление в корзину
                }
                .addOnFailureListener { e ->
                    // Обработка ошибок
                }
        }
    }
    fun removeFromCart(cartId: Int, productSku: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val cartItemRef = db.collection("cart_items")

        // Ищем товар в cart_items
        cartItemRef
            .whereEqualTo("cartId", cartId)
            .whereEqualTo("productSku", productSku)
            .get()
            .addOnSuccessListener { result ->
                if (!(result.isEmpty)) {
                    val existingItem = result.documents.first()
                    // Удаляем товар из корзины
                    cartItemRef.document(existingItem.id).delete()
                        .addOnSuccessListener {
                            onSuccess() // Уведомление об успешном удалении
                        }
                        .addOnFailureListener { e ->
                            onFailure("Error removing item: ${e.message}")
                        }
                } else {
                    onFailure("Item not found in cart.")
                }
            }
            .addOnFailureListener { e ->
                onFailure("Error fetching item: ${e.message}")
            }
    }



    // Получение всех товаров из корзины
    fun getCartItems(cartId: Int, onCartItemsLoaded: (List<CartItem>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("cart_items")
            .whereEqualTo("cartId", cartId)
            .get()
            .addOnSuccessListener { result ->
                val cartItems = result.map { document ->
                    document.toObject(CartItem::class.java)
                }
                onCartItemsLoaded(cartItems)
            }
            .addOnFailureListener { e ->
                Log.e("Cart", "Error fetching cart items", e)
            }
    }

    // Получение общей стоимости корзины
    fun getTotalPrice(cartId: Int, onPriceCalculated: (BigDecimal) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("cart_items")
            .whereEqualTo("cart_id", cartId)
            .get()
            .addOnSuccessListener { result ->
                var totalPrice = BigDecimal.ZERO
                result.forEach { document ->
                    val cartItem = document.toObject(CartItem::class.java)
                    // Получаем цену товара из таблицы Products
                    db.collection("products").document(cartItem.productSku.toString()).get()
                        .addOnSuccessListener { productDoc ->
                            val product = productDoc.toObject(Product::class.java)
                            if (product != null) {
                                //  totalPrice += product.price.(BigDecimal(cartItem.quantity))
                            }
                        }
                }
                onPriceCalculated(totalPrice)
            }
    }
    fun updateCartItemQuantity(cartId: Int, productSku: Int, newQuantity: Int) {
        val db = FirebaseFirestore.getInstance()
        val cartItemRef = db.collection("cart_items")

        // Ищем товар в cart_items
        cartItemRef
            .whereEqualTo("cartId", cartId)
            .whereEqualTo("productSku", productSku)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Log.d("Cart", "Item not found in cart.")
                } else {
                    // Если товар найден, обновляем его количество
                    val existingItem = result.documents.first()
                    val updatedItem = existingItem.toObject(CartItem::class.java)!!.copy(quantity = newQuantity)

                    // Обновляем cart_item с новым количеством
                    cartItemRef.document(existingItem.id).set(updatedItem)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Cart", "Error updating item quantity", e)
            }
    }
    fun removeFromCart(cartId: Int, productSku: Int) {
        val db = FirebaseFirestore.getInstance()
        val cartItemRef = db.collection("cart_items")

        // Ищем товар в cart_items
        cartItemRef
            .whereEqualTo("cartId", cartId)
            .whereEqualTo("productSku", productSku)
            .get()
            .addOnSuccessListener { result ->
                if (!(result.isEmpty)) {
                    val existingItem = result.documents.first()
                    // Удаляем товар из корзины
                    cartItemRef.document(existingItem.id).delete()
                }
            }
            .addOnFailureListener { e ->
                Log.e("Cart", "Error removing item from cart", e)
            }
    }

    fun addToCartWithCustomization(
        cartId: Int,
        imageUrl: String?,
        quantity: Int,
        customization: Customization,
        category: String
    ) {
        val db = FirebaseFirestore.getInstance()

        // Сначала сохраняем кастомизацию в коллекцию "customization"
        val customizationRef = db.collection("customization").document()

        // Создаем кастомизацию
        val customizationData = Customization(
            category = category, // Убедитесь, что category передается правильно (tshirt или hoodie)
            imageUrl = imageUrl ?: "", // Используем переданную ссылку на изображение
            text = customization.text, // Используем текст из переданных данных
            textColor = customization.textColor, // Цвет текста из переданных данных
            textPosition = customization.textPosition, // Положение текста
            font = customization.font // Шрифт текста
        )

        // Сохраняем кастомизацию в Firestore
        customizationRef.set(customizationData)
            .addOnSuccessListener {
                // После того, как кастомизация сохранена, получаем ID этой кастомизации
                val customizationId = customizationRef.id

                // Теперь добавляем товар в корзину
                val cartItem = CartItem(cartId, 0, quantity, customizationId) // Пример с productSku = 0, если это кастомный товар
                db.collection("cart_items")
                    .add(cartItem)
                    .addOnSuccessListener {
                        // Успешно добавлено в корзину
                        Log.d("Cart", "Товар с кастомизацией добавлен в корзину")
                    }
                    .addOnFailureListener { e ->
                        // Обработка ошибок
                        Log.e("Cart", "Ошибка добавления товара в корзину", e)
                    }
            }
            .addOnFailureListener { e ->
                // Обработка ошибок при сохранении кастомизации
                Log.e("Customization", "Ошибка сохранения кастомизации", e)
            }
    }



}