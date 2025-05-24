package com.app.vibess.data.model

data class Product(
    val name: String = "",
    val category: String = "",
    val color: String = "",
    val image: String = "",
    val price: Double = 0.0,
    val sku: Int = 0,
    val stock: Int = 0
)

