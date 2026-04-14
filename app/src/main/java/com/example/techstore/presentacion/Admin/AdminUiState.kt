package com.example.techstore.presentacion.Admin

import com.example.techstore.domain.model.Producto

data class AdminUiState(
    val isEditMode: Boolean = false,
    val productoId: Int? = null,
    val productos: List<Producto> = emptyList(),

    val nombre: String = "",
    val marca: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val stock: String = "",
    val imagenUrl: String = "",
    val categoriaId: String = "",

    val procesador: String = "",
    val ram: String = "",
    val almacenamiento: String = "",

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String? = null
)