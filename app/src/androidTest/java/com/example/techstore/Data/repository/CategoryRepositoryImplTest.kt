package com.example.techstore.Data.repository

import com.example.techstore.data.local.dao.CategoryDao
import com.example.techstore.data.local.entities.CategoryEntity
import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.categoria.CategoriaDTO
import com.example.techstore.data.repository.CategoryRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CategoryRepositoryImplTest {

    private val dao = mockk<CategoryDao>(relaxed = true)
    private val api = mockk<TechStoreApi>(relaxed = true)
    private lateinit var repository: CategoryRepositoryImpl

    @Before
    fun setup() {
        repository = CategoryRepositoryImpl(dao, api)
    }

    @Test
    fun getCategories_obtieneDatosDelDaoYMapeaCorrectamente() = runBlocking {
        val categoriasFalsas = listOf(
            CategoryEntity(id = 1, nombre = "Smartphones", imagenIconoUrl = "url")
        )
        every { dao.getAllCategories() } returns flowOf(categoriasFalsas)

        val resultado = repository.getCategories().first()

        assertEquals(1, resultado.size)
        assertEquals("Smartphones", resultado[0].nombre)
    }

    @Test
    fun refreshCategories_llamaApiLimpiaDaoEInsertaNuevasCategorias() = runBlocking {
        val categoriasApi = listOf(
            CategoriaDTO(id = 1, nombre = "Laptops", imagenIconoUrl = "url")
        )
        coEvery { api.getCategorias() } returns categoriasApi

        val resultado = repository.refreshCategories()

        assertTrue(resultado.isSuccess)
        coVerify(exactly = 1) { dao.clearCategories() }
        coVerify(exactly = 1) { dao.insertCategories(any()) }
    }
}