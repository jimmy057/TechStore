package com.example.techstore.data.repository

import com.example.techstore.data.local.dao.CartDao
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.data.local.mapper.toDomain
import com.example.techstore.data.local.mapper.toEntity
import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.pedido.DetallePedidoDTO
import com.example.techstore.data.remote.Dto.pedido.PedidoDto
import com.example.techstore.domain.model.CartItem
import com.example.techstore.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val api: TechStoreApi,
    private val sessionDataStore: SessionDataStore
) : CartRepository {

    override fun getCart(): Flow<List<CartItem>> {
        return cartDao.getCartItemsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToCartLocal(item: CartItem): Result<Unit> {
        return try {
            val usuarioId = sessionDataStore.getUserId().first() ?: 0
            val localItems = cartDao.getCartOnce()
            val existingItem = localItems.find { it.productoId == item.productoId }

            if (existingItem != null) {
                val updatedEntity = existingItem.copy(cantidad = existingItem.cantidad + 1)
                cartDao.insertOrUpdate(updatedEntity)
            } else {
                val entity = item.toEntity(usuarioId = usuarioId, isSynced = false)
                cartDao.insertOrUpdate(entity)
            }

            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun decrementQuantity(productoId: Int): Result<Unit> {
        return try {
            val localItems = cartDao.getCartOnce()
            val item = localItems.find { it.productoId == productoId }
            if (item != null && item.cantidad > 1) {
                cartDao.insertOrUpdate(item.copy(cantidad = item.cantidad - 1))
            } else if (item != null && item.cantidad == 1) {
                cartDao.deleteItem(productoId)
            }
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun removeFromCart(productoId: Int): Result<Unit> {
        return try {
            cartDao.deleteItem(productoId)
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun procesarCompraFinal(direccion: String, metodoPago: String): Result<Unit> {
        return try {
            val usuarioId = sessionDataStore.getUserId().first() ?: 0

            if (usuarioId == 0) {
                return Result.failure(Exception("Sesión inválida. Inicia sesión de nuevo."))
            }

            val localItems = cartDao.getCartOnce()
            if (localItems.isEmpty()) return Result.failure(Exception("El carrito está vacío"))

            android.util.Log.d("REPO_CART", "!!! DISPARANDO COMPRA REAL AL SERVIDOR !!!")

            val detallesDto = localItems.map {
                DetallePedidoDTO(
                    id = 0,
                    pedidoId = 0,
                    productoId = it.productoId,
                    producto = null,
                    cantidad = it.cantidad,
                    precioUnitario = it.precio
                )
            }

            val pedidoRequest = PedidoDto(
                id = 0,
                fecha = null,
                total = detallesDto.sumOf { it.precioUnitario * it.cantidad },
                estado = "Pendiente",
                direccionEnvio = direccion,
                metodoPago = metodoPago,
                usuarioId = usuarioId,
                detalles = detallesDto
            )

            val response = api.crearPedido(pedidoRequest)

            if (response.isSuccessful) {
                cartDao.clearCart()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error en servidor: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}