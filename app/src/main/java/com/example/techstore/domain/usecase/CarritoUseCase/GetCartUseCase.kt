package com.example.techstore.domain.usecase.CarritoUseCase

import com.example.techstore.domain.model.CartItem
import com.example.techstore.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> {
        return repository.getCart()
    }
}