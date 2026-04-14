package com.example.techstore.domain.usecase.CarritoUseCase

import com.example.techstore.domain.repository.CartRepository

class DecrementQuantityUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(productoId: Int) {
        repository.decrementQuantity(productoId)
    }
}