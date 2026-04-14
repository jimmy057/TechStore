package com.example.techstore.presentacion.Order

import com.example.techstore.domain.model.Order

data class OrderUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)