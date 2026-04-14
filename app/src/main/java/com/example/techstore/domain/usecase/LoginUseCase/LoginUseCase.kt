package com.example.techstore.domain.usecase.LoginUseCase

import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val sessionDataStore: SessionDataStore
) {
    suspend operator fun invoke(email: String, clave: String): Result<Unit> {
        val result = repository.login(email, clave)

        return if (result.isSuccess) {
            val authResponse = result.getOrNull()

            if (authResponse != null && authResponse.token.isNotEmpty()) {
                sessionDataStore.saveSession(
                    userId = authResponse.usuarioId,
                    name = authResponse.nombre,
                    email = authResponse.email,
                    token = authResponse.token,
                    role = authResponse.rol
                )
                Result.success(Unit)
            } else {
                Result.failure(Exception("El servidor no devolvió un token válido."))
            }
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Error desconocido"))
        }
    }
}