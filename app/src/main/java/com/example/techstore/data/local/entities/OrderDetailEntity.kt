package com.example.techstore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedido_detalles")
data class OrderDetailEntity(
    @PrimaryKey val id: Int,
    val pedidoId: Int,
    val productoId: Int,
    val nombreProducto: String?,
    val cantidad: Int,
    val precioUnitario: Double
)