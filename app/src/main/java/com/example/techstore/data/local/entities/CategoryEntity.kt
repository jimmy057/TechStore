package com.example.techstore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val imagenIconoUrl: String?
)