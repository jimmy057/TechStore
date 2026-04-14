package com.example.techstore.domain.model

data class Favorite(
    val productoId: Int,
    val nombre: String?,
    val precio: Double?,
    val imagenUrl: String?
)