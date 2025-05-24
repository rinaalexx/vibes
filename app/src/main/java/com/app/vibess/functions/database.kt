package com.app.vibess.functions

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



