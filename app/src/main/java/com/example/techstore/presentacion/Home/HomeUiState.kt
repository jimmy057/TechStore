package com.example.techstore.presentacion.Home

import com.example.techstore.domain.model.Producto

data class HomeState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null,
    val isAdmin: Boolean = false,
    val activeCategory: String? = null
)