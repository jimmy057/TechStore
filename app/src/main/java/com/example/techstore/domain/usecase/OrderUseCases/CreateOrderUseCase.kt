package com.example.techstore.domain.usecase.OrderUseCases

import com.example.techstore.domain.model.CartItem
import com.example.techstore.domain.repository.OrderRepository

class CreateOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(items: List<CartItem>, total: Double, userId: Int): Result<Unit> {
        return repository.createOrder(items, total, userId)
    }
}