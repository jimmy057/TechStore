package com.example.techstore.domain.usecase.OrderUseCases

data class OrderUseCases(
    val getOrderHistory: GetOrderHistoryUseCase,
    val refreshOrderHistory: RefreshOrderHistoryUseCase,
    val createOrder: CreateOrderUseCase
)