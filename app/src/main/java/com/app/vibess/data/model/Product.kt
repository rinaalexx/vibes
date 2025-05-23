package com.app.vibess.data.model
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String? = null,
    val category: String = "all"  // например: "tshirts", "hoodie", "all"
)
