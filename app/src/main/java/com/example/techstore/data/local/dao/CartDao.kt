package com.example.techstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.techstore.data.local.entities.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItemsFlow(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart_items WHERE isSynced = 0")
    suspend fun getUnsyncedItems(): List<CartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartEntity)

    @Query("UPDATE cart_items SET isSynced = 1 WHERE productoId = :productoId")
    suspend fun markAsSynced(productoId: Int)

    @Query("DELETE FROM cart_items WHERE productoId = :productoId")
    suspend fun deleteItem(productoId: Int)

    @Query("SELECT * FROM cart_items")
    suspend fun getCartOnce(): List<CartEntity>

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}