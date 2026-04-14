package com.example.techstore.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class OrderWithDetails(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "pedidoId"
    )
    val details: List<OrderDetailEntity>
)