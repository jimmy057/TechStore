package com.example.techstore.domain.repository

import com.example.techstore.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCart(): Flow<List<CartItem>>
    suspend fun addToCartLocal(item: CartItem): Result<Unit>
    suspend fun removeFromCart(productoId: Int): Result<Unit>
    suspend fun decrementQuantity(productoId: Int): Result<Unit>

    suspend fun procesarCompraFinal(direccion: String, metodoPago: String): Result<Unit>
}