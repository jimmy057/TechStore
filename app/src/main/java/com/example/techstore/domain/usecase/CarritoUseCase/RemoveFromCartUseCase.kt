package com.example.techstore.domain.usecase.CarritoUseCase

import com.example.techstore.domain.repository.CartRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(productoId: Int): Result<Unit> {
        return repository.removeFromCart(productoId)
    }
}