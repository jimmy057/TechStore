package com.example.techstore.data.local.mapper

import com.example.techstore.data.local.entities.CategoryEntity
import com.example.techstore.data.remote.Dto.categoria.CategoriaDTO
import com.example.techstore.domain.model.Category

fun CategoriaDTO.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        nombre = this.nombre ?: "General",
        imagenIconoUrl = this.imagenIconoUrl
    )
}

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = this.id,
        nombre = this.nombre,
        imagenIconoUrl = this.imagenIconoUrl
    )
}