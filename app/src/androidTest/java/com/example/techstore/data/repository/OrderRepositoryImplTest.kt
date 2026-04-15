package com.example.techstore.data.repository

import com.example.techstore.data.local.dao.CartDao
import com.example.techstore.data.local.dao.OrderDao
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.pedido.PedidoDto
import com.example.techstore.domain.model.CartItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class OrderRepositoryImplTest {

    private val orderDao = mockk<OrderDao>(relaxed = true)
    private val cartDao = mockk<CartDao>(relaxed = true)
    private val api = mockk<TechStoreApi>(relaxed = true)
    private val sessionDataStore = mockk<SessionDataStore>(relaxed = true)
    private lateinit var repository: OrderRepositoryImpl

    @Before
    fun setup() {
        repository = OrderRepositoryImpl(orderDao, cartDao, api, sessionDataStore)
        every { sessionDataStore.getUserId() } returns flowOf(1)
    }

    @Test
    fun createOrder_enviaPedidoApiYLimpiaCarritoSiExitoso() = runBlocking {
        val itemsDelCarrito = listOf(
            CartItem(productoId = 1, nombre = "Mouse", precio = 20.0, cantidad = 1, imagenUrl = "")
        )
        coEvery { api.crearPedido(any()) } returns Response.success(mockk<PedidoDto>(relaxed = true))

        val resultado = repository.createOrder(items = itemsDelCarrito, total = 20.0, userId = 1)

        assertTrue(resultado.isSuccess)

        coVerify(exactly = 1) { api.crearPedido(any()) }
        coVerify(exactly = 1) { cartDao.clearCart() }
    }
}