package com.example.techstore.presentacion.Category

import com.example.techstore.domain.model.Category

data class CategoryUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)