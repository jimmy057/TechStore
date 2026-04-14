package com.example.techstore.data.repository

import com.example.techstore.data.local.dao.FavoriteDao
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.data.local.entities.FavoriteEntity
import com.example.techstore.data.local.mapper.toEntity
import com.example.techstore.data.local.mapper.toFavoriteDomain
import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.domain.model.Favorite
import com.example.techstore.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao,
    private val api: TechStoreApi,
    private val sessionDataStore: SessionDataStore
) : FavoriteRepository {

    override fun getFavorites(): Flow<List<Favorite>> = flow {
        val userId = sessionDataStore.getUserId().first() ?: 0

        emitAll(dao.getFavoriteProductsWithDetails(userId).map { entities ->
            entities.map { it.toFavoriteDomain() }
        })
    }

    override suspend fun toggleFavorite(productoId: Int): Result<Unit> {
        return try {
            val userId = sessionDataStore.getUserId().first() ?: 0
            val isFav = dao.isFavorite(productoId, userId)

            if (isFav) {
                dao.deleteFavorite(
                    FavoriteEntity(
                        usuarioId = userId,
                        productoId = productoId
                    )
                )
            } else {
                dao.insertFavorite(
                    FavoriteEntity(
                        usuarioId = userId,
                        productoId = productoId,
                        isSynced = false
                    )
                )
            }
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun refreshFavorites(): Result<Unit> {
        return try {
            val userId = sessionDataStore.getUserId().first() ?: 0
            if (userId == 0) return Result.failure(Exception("Usuario no logueado"))

            val response = api.getFavoritos(userId)
            val entities = response.map { it.toEntity(isSynced = true) }

            entities.forEach { dao.insertFavorite(it) }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}