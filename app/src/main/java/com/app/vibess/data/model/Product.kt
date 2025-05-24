package com.app.vibess.data.model
import androidx.compose.runtime.mutableStateListOf
data class Product(
    val name: String = "",
    val category: String = "",
    val color: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val sku: Int = 0,
    val stock: Int = 0
)



// Корзина — список товаров (глобально, например, в синглтоне или ViewModel)
val cartItems = mutableStateListOf<Product>()

// Функция добавления в корзину
fun addToCart(product: Product) {
    cartItems.add(product)
}

// Функция удаления из корзины (по id или объекту)
fun removeFromCart(product: Product) {
    cartItems.remove(product)
}


