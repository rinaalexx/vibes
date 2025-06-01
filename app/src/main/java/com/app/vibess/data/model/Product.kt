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

data class Cart(
    val userId: Int,
    val items: List<CartItem> = emptyList() // Список товаров в корзине
)

data class CartItem(
    val cartId: Int = 0,
    val productSku: Int = 0,
    val quantity: Int = 0,
    val customizationId: String = ""
)



data class Customization(
    val category: String,      // Категория товара (tshirt или hoodie)
    val imageUrl: String,     // Ссылка на изображение
    val text: String,         // Текст на товаре
    val textColor: String,    // Цвет текста
    val textPosition: String, // Положение текста (top/bottom)
    val font: String          // Шрифт текста
)
data class User(
    val firstName: String? = "",
    val lastName: String? = "",
    val orderHistory: List<String> = emptyList(),  // Пустой список заказов по умолчанию
    val activeOrders: List<String> = emptyList(),   // Пустой список активных заказов по умолчанию
    val email: String? = "",
    val password: String = ""
)
data class Order(
    val orderId: String = "",  // Уникальный идентификатор заказа
    val userId: String = "",  // Идентификатор пользователя
    val totalPrice: Double = 0.0,  // Общая цена заказа
    val status: String = "Processing",  // Статус заказа (обрабатывается, отправлен и т.д.)
    val shippingMethod: String = "",  // Способ доставки
    val shippingAddress: String = "",  // Адрес доставки
    val firstName: String = "",  // Имя заказчика
    val lastName: String = ""  // Фамилия заказчика
)

data class OrderItem(
    val orderId: String? = "",  // Ссылка на заказ
    val items: List<CartItem> = emptyList()  // Список товаров в корзине
)


data class Shipping(
    val orderId :String? = "",
    val shippingMethod: String = "",
    val shippingAddress: String = "",
    val shippingStatus: String = "Pending"
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


