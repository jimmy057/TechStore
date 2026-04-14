package com.example.techstore.presentacion.Favorite

import com.example.techstore.domain.model.Favorite

data class FavoriteUiState(
    val favorites: List<Favorite> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)