package com.example.techstore.presentacion.Detail

import com.example.techstore.domain.model.Producto

data class DetailUiState(
    val producto: Producto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val cartMessage: String? = null,
    val isFavorite: Boolean = false
)