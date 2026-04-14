package com.example.techstore.domain.model

data class CartItem(
    val productoId: Int,
    val nombre: String,
    val precio: Double,
    val imagenUrl: String,
    val cantidad: Int
)