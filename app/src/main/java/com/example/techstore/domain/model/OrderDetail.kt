package com.example.techstore.domain.model

data class OrderDetail(
    val id: Int,
    val pedidoId: Int,
    val productoId: Int,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double
)