package com.example.techstore.domain.usecase.OrderUseCases

import com.example.techstore.domain.model.Order
import com.example.techstore.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetOrderHistoryUseCase(
    private val repository: OrderRepository
) {
    operator fun invoke(): Flow<List<Order>> {
        return repository.getOrderHistory()
    }
}