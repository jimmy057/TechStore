package com.example.techstore.data.local.mapper

import com.example.techstore.data.local.entities.CartEntity
import com.example.techstore.domain.model.CartItem

fun CartEntity.toDomain(): CartItem {
    return CartItem(
        productoId = this.productoId,
        nombre = this.nombre,
        precio = this.precio,
        imagenUrl = this.imagenUrl,
        cantidad = this.cantidad
    )
}

fun CartItem.toEntity(usuarioId: Int, isSynced: Boolean = false): CartEntity {
    return CartEntity(
        productoId = this.productoId,
        nombre = this.nombre,
        precio = this.precio,
        imagenUrl = this.imagenUrl,
        cantidad = this.cantidad,
        usuarioId = usuarioId,
        isSynced = isSynced
    )
}