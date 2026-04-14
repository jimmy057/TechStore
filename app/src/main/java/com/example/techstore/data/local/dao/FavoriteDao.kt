package com.example.techstore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techstore.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favoritos WHERE usuarioId = :userId")
    fun getFavoritesByUser(userId: Int): Flow<List<FavoriteEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)
    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)
    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE productoId = :prodId AND usuarioId = :userId)")
    suspend fun isFavorite(prodId: Int, userId: Int): Boolean

    @Query("SELECT * FROM favoritos WHERE isSynced = 0")
    suspend fun getUnsyncedFavorites(): List<FavoriteEntity>

    @Query("UPDATE favoritos SET isSynced = 1 WHERE productoId = :prodId AND usuarioId = :userId")
    suspend fun markAsSynced(prodId: Int, userId: Int)
    @Query("""
        SELECT productos.* FROM productos 
        INNER JOIN favoritos ON productos.id = favoritos.productoId 
        WHERE favoritos.usuarioId = :userId
    """)
    fun getFavoriteProductsWithDetails(userId: Int): Flow<List<com.example.techstore.data.local.entities.ProductoEntity>>
}