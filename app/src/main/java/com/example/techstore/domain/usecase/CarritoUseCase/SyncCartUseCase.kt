package com.example.techstore.domain.usecase.CarritoUseCase

import com.example.techstore.domain.repository.CartRepository

class SyncCartUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(direccion: String, metodoPago: String) =
        repository.procesarCompraFinal(direccion, metodoPago)
}