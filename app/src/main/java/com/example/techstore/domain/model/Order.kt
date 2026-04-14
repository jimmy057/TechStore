package com.example.techstore.domain.model

data class Order(
    val id: Int,
    val fecha: String,
    val total: Double,
    val estado: String,
    val direccionEnvio: String,
    val metodoPago: String,
    val usuarioId: Int,
    val detalles: List<OrderDetail> = emptyList()
)