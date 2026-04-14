package com.example.techstore.data.local.entities

import androidx.room.Entity

@Entity(
    tableName = "favoritos",
    primaryKeys = ["usuarioId", "productoId"]
)
data class FavoriteEntity(
    val id: Int = 0,
    val usuarioId: Int,
    val productoId: Int,
    val isSynced: Boolean = true
)