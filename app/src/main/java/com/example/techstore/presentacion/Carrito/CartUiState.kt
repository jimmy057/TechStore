package com.example.techstore.presentacion.Carrito

import com.example.techstore.domain.model.CartItem

data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val total: Double = 0.0
)