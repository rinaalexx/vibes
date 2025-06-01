package com.app.vibess.functions

import com.app.vibess.data.model.CartItem
import com.google.firebase.firestore.FirebaseFirestore
import com.app.vibess.data.model.Product

fun addProductToFirestore(product: Product, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("products")  // имя коллекции в Firestore
        .add(product)
        .addOnSuccessListener { documentReference ->
            onSuccess()
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun getProductsFromFirestore(onResult: (List<Product>) -> Unit, onFailure: (Exception) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("products")
        .get()
        .addOnSuccessListener { result ->
            val products = result.documents.mapNotNull { it.toObject(Product::class.java) }
            onResult(products)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
    }


fun findProductById(
    sku: Int,
    onSuccess: (Product) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    // Пытаемся найти продукт по SKU в коллекции products
    db.collection("products")
        .whereEqualTo("sku", sku)
        .get()
        .addOnSuccessListener { result ->
            // Если продукт найден, передаем его в onSuccess
            val product = result.documents.firstOrNull()?.toObject(Product::class.java)
            if (product != null) {
                onSuccess(product)
            } else {
                onFailure(Exception("Продукт не найден"))
            }
        }
        .addOnFailureListener { exception ->
            // Если произошла ошибка при запросе, передаем ошибку в onFailure
            onFailure(exception)
        }
}
fun findProductByIdInCart(
    sku: Int,
    onSuccess: (Product) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    // Пытаемся найти продукт по SKU в коллекции products
    db.collection("products")
        .whereEqualTo("sku", sku)
        .get()
        .addOnSuccessListener { result ->
            // Если продукт найден, передаем его в onSuccess
            val product = result.documents.firstOrNull()?.toObject(Product::class.java)
            if (product != null) {
                onSuccess(product)
            } else {
                onFailure(Exception("Продукт не найден"))
            }
        }
        .addOnFailureListener { exception ->
            // Если произошла ошибка при запросе, передаем ошибку в onFailure
            onFailure(exception)
        }
}
fun getProductPrice(productSku: Int, onSuccess: (Double) -> Unit, onFailure: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("products")
        .whereEqualTo("sku", productSku)
        .get()
        .addOnSuccessListener { result ->
            val product = result.documents.firstOrNull()?.toObject(Product::class.java)
            if (product != null) {
                onSuccess(product.price) // Возвращаем цену товара
            } else {
                onFailure("Product not found") // Ошибка если товар не найден
            }
        }
        .addOnFailureListener { e ->
            onFailure("Error fetching product price: ${e.message}")
        }
}
fun getCartItemsByUserId(userId: Int, onSuccess: (List<CartItem>) -> Unit, onFailure: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("cart_items")
        .whereEqualTo("cartId", userId)  // Ищем товары по cartId, который равен userId
        .get()
        .addOnSuccessListener { result ->
            val cartItems = result.documents.mapNotNull { document ->
                document.toObject(CartItem::class.java)
            }
            onSuccess(cartItems)
        }
        .addOnFailureListener { exception ->
            onFailure(exception.message ?: "Error fetching cart items")
        }
}




