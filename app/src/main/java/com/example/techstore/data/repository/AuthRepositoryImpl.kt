package com.example.techstore.data.repository

import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.Login.LoginRequest
import com.example.techstore.domain.repository.AuthRepository
import com.example.techstore.data.remote.Dto.Login.AuthResponse
import com.example.techstore.data.remote.Dto.Register.RegisterRequest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: TechStoreApi
) : AuthRepository {

    override suspend fun login(email: String, clave: String): Result<AuthResponse> {
        return try {
            val request = LoginRequest(email = email, clave = clave)

            val response = api.login(request)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Credenciales incorrectas o error en el servidor"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo conectar al servidor. Revisa tu conexión."))
        }
    }

    override suspend fun register(nombre: String, email: String, clave: String): Result<Unit> {
        return try {
            val request = RegisterRequest(nombre = nombre, email = email, clave = clave)

            val response = api.register(request)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al registrar: El correo podría ya estar en uso."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo conectar al servidor."))
        }
    }
}