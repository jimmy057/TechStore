package com.example.techstore.data.repository

import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.Login.AuthResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryImplTest {

    private val api = mockk<TechStoreApi>(relaxed = true)
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setup() {
        repository = AuthRepositoryImpl(api)
    }

    @Test
    fun login_retornaSuccessConAuthResponseSiApiEsExitosa() = runBlocking {
        val authResponseFalsa = AuthResponse(token = "jwt_token_falso", usuario = mockk(relaxed = true))
        coEvery { api.login(any()) } returns Response.success(authResponseFalsa)

        val resultado = repository.login("test@test.com", "123456")

        assertTrue(resultado.isSuccess)
        assertEquals("jwt_token_falso", resultado.getOrNull()?.token)
        coVerify(exactly = 1) { api.login(any()) }
    }

    @Test
    fun register_retornaSuccessSiApiEsExitosa() = runBlocking {
        coEvery { api.register(any()) } returns Response.success(Unit)

        val resultado = repository.register("Jimmy", "jimmy@test.com", "123456")

        assertTrue(resultado.isSuccess)
        coVerify(exactly = 1) { api.register(any()) }
    }
}