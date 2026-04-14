package com.example.techstore.domain.repository

import com.example.techstore.domain.model.CartItem
import com.example.techstore.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrderHistory(): Flow<List<Order>>
    suspend fun refreshOrderHistory(): Result<Unit>
    suspend fun createOrder(items: List<CartItem>, total: Double, userId: Int): Result<Unit>
}