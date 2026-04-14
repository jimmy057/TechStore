package com.example.techstore.data.remote.Dto.Producto

data class CreateProductoRequest(
    val id: Int = 0,
    val nombre: String,
    val marca: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val imagenUrl: String,
    val categoriaId: Int,
    val procesador: String = "",
    val ram: String = "",
    val precioOferta: Double? = null,
    val calificacion: Double = 5.0,
    val almacenamiento: String = "",
    val fechaCreado: String = "2024-01-01T00:00:00"
)