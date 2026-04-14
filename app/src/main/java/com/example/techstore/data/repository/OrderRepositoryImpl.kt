package com.example.techstore.data.repository

import com.example.techstore.data.local.dao.CartDao
import com.example.techstore.data.local.dao.OrderDao
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.data.local.mapper.toDomain
import com.example.techstore.data.local.mapper.toEntity
import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.pedido.DetallePedidoDTO
import com.example.techstore.data.remote.Dto.pedido.PedidoDto
import com.example.techstore.domain.model.CartItem
import com.example.techstore.domain.model.Order
import com.example.techstore.domain.repository.OrderRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val dao: OrderDao,
    private val cartDao: CartDao,
    private val api: TechStoreApi,
    private val sessionDataStore: SessionDataStore
) : OrderRepository {

    override fun getOrderHistory(): Flow<List<Order>> = flow {
        val userId = sessionDataStore.getUserId().first() ?: 0
        emitAll(dao.getOrdersWithDetails(userId).map { list ->
            list.map { relation ->
                relation.order.toDomain(relation.details.map { it.toDomain() })
            }
        })
    }

    override suspend fun refreshOrderHistory(): Result<Unit> {
        return try {
            val userId = sessionDataStore.getUserId().first() ?: 0
            if (userId == 0) return Result.failure(Exception("Usuario no identificado"))

            val response = api.getPedidos(userId)
            response.forEach { pedidoDto ->
                dao.saveOrderWithDetails(
                    order = pedidoDto.toEntity(),
                    details = pedidoDto.detalles?.map { it.toEntity() } ?: emptyList()
                )
            }
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun createOrder(
        items: List<CartItem>,
        total: Double,
        userId: Int
    ): Result<Unit> {
        return try {

            val detallesDto = items.map { item ->
                DetallePedidoDTO(
                    id = 0,
                    pedidoId = 0,
                    productoId = item.productoId,
                    cantidad = item.cantidad,
                    precioUnitario = item.precio,
                    producto = null
                )
            }

            val request = PedidoDto(
                id = 0,
                fecha = null,
                total = total,
                estado = "Pendiente",
                direccionEnvio = "Dirección por defecto",
                metodoPago = "Tarjeta",
                usuarioId = userId,
                detalles = detallesDto
            )

            val pedidoResponse = api.crearPedido(request)

            if (pedidoResponse.isSuccessful) {
                cartDao.clearCart()
                refreshOrderHistory()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error en el servidor: ${pedidoResponse.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}