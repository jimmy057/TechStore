package com.example.techstore.data.repository

import com.example.techstore.data.local.dao.FavoriteDao
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.data.remote.Api.TechStoreApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoriteRepositoryImplTest {

    private val dao = mockk<FavoriteDao>(relaxed = true)
    private val api = mockk<TechStoreApi>(relaxed = true)
    private val sessionDataStore = mockk<SessionDataStore>(relaxed = true)
    private lateinit var repository: FavoriteRepositoryImpl

    @Before
    fun setup() {
        repository = FavoriteRepositoryImpl(dao, api, sessionDataStore)
        every { sessionDataStore.getUserId() } returns flowOf(1)
    }

    @Test
    fun toggleFavorite_eliminaElProductoSiYaEraFavorito() = runBlocking {
        coEvery { dao.isFavorite(productoId = 10, usuarioId = 1) } returns true

        val resultado = repository.toggleFavorite(10)

        assertTrue(resultado.isSuccess)
        coVerify(exactly = 1) { dao.deleteFavorite(any()) }
        coVerify(exactly = 0) { dao.insertFavorite(any()) }
    }

    @Test
    fun toggleFavorite_insertaElProductoSiNoEraFavorito() = runBlocking {
        coEvery { dao.isFavorite(productoId = 10, usuarioId = 1) } returns false

        val resultado = repository.toggleFavorite(10)

        assertTrue(resultado.isSuccess)
        coVerify(exactly = 1) { dao.insertFavorite(any()) }
        coVerify(exactly = 0) { dao.deleteFavorite(any()) }
    }
}