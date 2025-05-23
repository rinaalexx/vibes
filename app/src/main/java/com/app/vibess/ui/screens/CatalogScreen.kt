package com.app.vibess.ui.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.app.vibess.data.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun CatalogScreen(navController: NavController) {
    val products = remember { mutableStateOf<List<Product>>(emptyList()) }

    // Загружаем данные из Firestore
    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val productList = result.documents.map { document ->
                    Product(
                        name = document.getString("name") ?: "",
                        category = document.getString("category") ?: "",
                        color = document.getString("color") ?: "",
                        image = document.getString("image") ?: "",
                        price = document.getDouble("price") ?: 0.0,
                        sku = document.getLong("sku")?.toInt() ?: 0,
                        stock = document.getLong("stock")?.toInt() ?: 0
                    )
                }
                products.value = productList
            }
            .addOnFailureListener { exception ->
                // Обработать ошибку загрузки
            }
    }


}


