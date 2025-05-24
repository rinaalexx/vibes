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

