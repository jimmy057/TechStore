package com.example.techstore.data.local.mapper

import com.example.techstore.data.local.entities.ProductoEntity
import com.example.techstore.data.remote.Dto.Producto.ProductoDto
import com.example.techstore.domain.model.Producto

fun ProductoDto.toEntity(): ProductoEntity {
    return ProductoEntity(
        id = id,
        nombre = nombre,
        marca = marca ?: "",
        descripcion = descripcion ?: "",
        precio = precio,
        precioOferta = precioOferta,
        stock = stock ?: 0,
        imagenUrl = imagenUrl ?: "",
        categoria = nombreCategoria ?: "",
        categoriaId = categoriaId ?: 0,
        procesador = procesador ?: "N/A",
        ram = ram ?: "N/A",
        almacenamiento = almacenamiento ?: "N/A",
        calificacion = calificacion ?: 0.0,
        galeriaString = galeria?.joinToString(",") ?: "",
        isSynced = true
    )
}

fun ProductoEntity.toDomain(): Producto {
    return Producto(
        id = id,
        nombre = nombre,
        marca = marca ?: "",
        descripcion = descripcion ?: "",
        precio = precio,
        precioOferta = precioOferta,
        stock = stock,
        imagenUrl = imagenUrl ?: "",
        categoria = categoria ?: "",
        categoriaId = categoriaId,
        procesador = procesador ?: "N/A",
        ram = ram ?: "N/A",
        almacenamiento = almacenamiento ?: "N/A",
        calificacion = calificacion ?: 0.0,
        galeriaUrls = if (galeriaString.isNotEmpty()) galeriaString.split(",") else emptyList()
    )
}

fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = id,
        nombre = nombre,
        marca = marca,
        descripcion = descripcion,
        precio = precio,
        precioOferta = precioOferta,
        stock = stock,
        imagenUrl = imagenUrl,
        nombreCategoria = categoria,
        categoriaId = categoriaId,
        procesador = procesador,
        ram = ram,
        almacenamiento = almacenamiento,
        calificacion = calificacion,
        galeria = galeriaUrls
    )
}