package com.example.techstore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos")
data class OrderEntity(
    @PrimaryKey val id: Int,
    val fecha: String,
    val total: Double,
    val estado: String?,
    val direccionEnvio: String?,
    val metodoPago: String?,
    val usuarioId: Int,
    val isSynced: Boolean = true
)