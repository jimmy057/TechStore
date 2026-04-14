package com.example.techstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.techstore.data.local.entities.OrderDetailEntity
import com.example.techstore.data.local.entities.OrderEntity
import com.example.techstore.data.local.entities.OrderWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM pedidos WHERE usuarioId = :userId ORDER BY fecha DESC")
    fun getOrdersByUser(userId: Int): Flow<List<OrderEntity>>

    @Query("SELECT * FROM pedido_detalles WHERE pedidoId = :orderId")
    fun getOrderDetails(orderId: Int): Flow<List<OrderDetailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderDetails(details: List<OrderDetailEntity>)

    @Transaction
    suspend fun saveOrderWithDetails(order: OrderEntity, details: List<OrderDetailEntity>) {
        insertOrders(listOf(order))
        insertOrderDetails(details)
    }

    @Query("DELETE FROM pedidos")
    suspend fun clearOrders()

    @Transaction
    @Query("SELECT * FROM pedidos WHERE usuarioId = :userId ORDER BY fecha DESC")
    fun getOrdersWithDetails(userId: Int): Flow<List<OrderWithDetails>>
}