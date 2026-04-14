package com.example.techstore.domain.usecase.CarritoUseCase

import com.example.techstore.domain.model.CartItem
import com.example.techstore.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(item: CartItem): Result<Unit> {
        if (item.cantidad <= 0) {
            return Result.failure(Exception("La cantidad a comprar debe ser mayor a 0"))
        }
        if (item.precio < 0) {
            return Result.failure(Exception("El precio no puede ser negativo"))
        }

        return repository.addToCartLocal(item)
    }
}