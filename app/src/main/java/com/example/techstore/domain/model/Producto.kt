package com.example.techstore.domain.model

data class Producto(
    val id: Int,
    val nombre: String,
    val marca: String,
    val descripcion: String,
    val precio: Double,
    val precioOferta: Double?,
    val stock: Int,
    val imagenUrl: String,
    val categoria: String,
    val categoriaId: Int,
    val procesador: String,
    val ram: String,
    val almacenamiento: String,
    val calificacion: Double,
    val galeriaUrls: List<String>
)