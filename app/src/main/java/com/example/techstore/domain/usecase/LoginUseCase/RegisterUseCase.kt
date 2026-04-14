package com.example.techstore.domain.usecase.LoginUseCase

import com.example.techstore.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(nombre: String, email: String, clave: String): Result<Unit> {
        if (nombre.isBlank() || email.isBlank() || clave.isBlank()) {
            return Result.failure(Exception("Todos los campos son obligatorios"))
        }
        if (clave.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }
        if (!email.contains("@")) {
            return Result.failure(Exception("Correo electrónico inválido"))
        }

        return repository.register(nombre, email, clave)
    }
}