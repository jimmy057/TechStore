package com.example.techstore.Data.repository

import com.example.techstore.data.local.dao.CartDao
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.data.local.entities.CartEntity
import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.pedido.PedidoDto
import com.example.techstore.data.repository.CartRepositoryImpl
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

class CartRepositoryImplTest {

    private val cartDao = mockk<CartDao>(relaxed = true)
    private val api = mockk<TechStoreApi>(relaxed = true)
    private val sessionDataStore = mockk<SessionDataStore>(relaxed = true)
    private lateinit var repository: CartRepositoryImpl

    @Before
    fun setup() {
        repository = CartRepositoryImpl(cartDao, api, sessionDataStore)
        every { sessionDataStore.getUserId() } returns flowOf(1)
    }

    @Test
    fun procesarCompraFinal_vaciaElCarritoLocalSiLaApiRespondeExito() = runBlocking {
        val itemsFalsos = listOf(
            CartEntity(id = 1, productoId = 10, nombre = "Audifonos", precio = 50.0, cantidad = 2, imagenUrl = "", usuarioId = 1, isSynced = false)
        )
        coEvery { cartDao.getCartOnce() } returns itemsFalsos
        coEvery { api.crearPedido(any()) } returns Response.success(mockk<PedidoDto>(relaxed = true))

        val resultado = repository.procesarCompraFinal("Mi Casa", "Tarjeta")

        assertTrue(resultado.isSuccess)
        coVerify(exactly = 1) { api.crearPedido(any()) }
        coVerify(exactly = 1) { cartDao.clearCart() }
    }
}