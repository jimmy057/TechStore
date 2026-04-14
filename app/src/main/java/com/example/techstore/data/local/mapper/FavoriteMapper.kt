package com.example.techstore.data.local.mapper

import com.example.techstore.data.local.entities.FavoriteEntity
import com.example.techstore.data.local.entities.ProductoEntity
import com.example.techstore.data.remote.Dto.Favorito.FavoritoDto
import com.example.techstore.domain.model.Favorite

fun FavoritoDto.toEntity(isSynced: Boolean = true): FavoriteEntity {
    return FavoriteEntity(
        usuarioId = this.usuarioId,
        productoId = this.productoId,
        isSynced = isSynced
    )
}

fun ProductoEntity.toFavoriteDomain(): Favorite {
    return Favorite(
        productoId = this.id,
        nombre = this.nombre,
        precio = this.precio,
        imagenUrl = this.imagenUrl
    )
}