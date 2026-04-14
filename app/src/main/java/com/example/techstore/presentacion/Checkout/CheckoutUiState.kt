package com.example.techstore.presentacion.Checkout

import com.example.techstore.domain.model.CartItem

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0
)