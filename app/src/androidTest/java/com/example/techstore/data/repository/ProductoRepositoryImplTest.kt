package com.example.techstore.data.repository

import com.example.techstore.data.local.dao.ProductoDao
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.data.local.entities.ProductoEntity
import com.example.techstore.data.remote.Dto.Producto.ProductoDto
import com.example.techstore.data.remote.ProductoRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.collections.emptyList

class ProductoRepositoryImplTest {

    private val remoteDataSource = mockk<ProductoRemoteDataSource>(relaxed = true)
    private val dao = mockk<ProductoDao>(relaxed = true)
    private val sessionDataStore = mockk<SessionDataStore>(relaxed = true)
    private lateinit var repository: ProductoRepositoryImpl

    @Before
    fun setup() {
        repository = ProductoRepositoryImpl(remoteDataSource, dao, sessionDataStore)
    }

    @Test
    fun getProductos_obtieneDatosDelDaoYMapeaCorrectamente() = runBlocking {
        val productosFalsos = listOf(
            ProductoEntity(id = 1, nombre = "Teclado", precio = 100.0, stock = 5, categoriaId = 1, calificacion = 5.0, marca = "", descripcion = "", imagenUrl = "", categoria = "", procesador = "", ram = "", almacenamiento = "", galeriaString = "", isSynced = true)
        )
        coEvery { dao.getProductos() } returns flowOf(productosFalsos)

        val resultado = repository.getProductos().first()

        assertEquals(1, resultado.size)
        assertEquals("Teclado", resultado[0].nombre)
        assertEquals(100.0, resultado[0].precio, 0.0)
    }

    @Test
    fun refreshProductos_obtieneDeApiLimpiaLocalesEInsertaNuevos() = runBlocking {
        val productosApi = listOf(
            ProductoDto(id = 1, nombre = "Monitor", precio = 200.0, stock = 10, categoriaId = 1, calificacion = 4.0, marca = "", descripcion = "", imagenUrl = "", categoria = "", procesador = "", ram = "", almacenamiento = "", galeriaUrls = emptyList(), precioOferta = null)
        )
        coEvery { dao.getUnsyncedProductos() } returns emptyList()
        coEvery { remoteDataSource.getProductos() } returns Response.success(productosApi)

        val resultado = repository.refreshProductos()

        assertTrue(resultado.isSuccess)
        coVerify(exactly = 1) { dao.clearSyncedProductos() }
        coVerify(exactly = 1) { dao.insertProductos(any()) }
    }
}