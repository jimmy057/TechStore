package com.example.techstore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val productoId: Int,
    val nombre: String,
    val precio: Double,
    val imagenUrl: String,
    val cantidad: Int,
    val usuarioId: Int,
    val isSynced: Boolean = false
)