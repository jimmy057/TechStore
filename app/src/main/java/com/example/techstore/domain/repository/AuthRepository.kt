package com.example.techstore.domain.repository

import com.example.techstore.data.remote.Dto.Login.AuthResponse

interface AuthRepository {
    suspend fun login(email: String, clave: String): Result<AuthResponse>

    suspend fun register(nombre: String, email: String, clave: String): Result<Unit>
}