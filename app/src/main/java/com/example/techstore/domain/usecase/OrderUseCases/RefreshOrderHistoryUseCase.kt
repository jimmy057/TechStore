package com.example.techstore.domain.usecase.OrderUseCases

import com.example.techstore.domain.repository.OrderRepository

class RefreshOrderHistoryUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.refreshOrderHistory()
    }
}