package com.example.techstore.domain.repository

import com.example.techstore.domain.model.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<Favorite>>
    suspend fun toggleFavorite(productoId: Int): Result<Unit>
    suspend fun refreshFavorites(): Result<Unit>
}