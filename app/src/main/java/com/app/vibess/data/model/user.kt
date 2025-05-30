package com.app.vibess.data.model

data class User(
    val firstName: String?,
    val lastName: String?,
    val orderHistory: List<String> = emptyList(),  // Пустой список заказов по умолчанию
    val activeOrders: List<String> = emptyList(),   // Пустой список активных заказов по умолчанию
    val email: String?,
    val password: String
)