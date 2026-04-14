package com.example.techstore.data.remote.Dto.Producto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductoDto(
    @Json(name = "id") val id: Int,
    @Json(name = "nombre") val nombre: String,
    @Json(name = "marca") val marca: String?,
    @Json(name = "descripcion") val descripcion: String?,
    @Json(name = "precio") val precio: Double,
    @Json(name = "precioOferta") val precioOferta: Double?,
    @Json(name = "stock") val stock: Int?,
    @Json(name = "imagenUrl") val imagenUrl: String?,
    @Json(name = "nombreCategoria") val nombreCategoria: String?,
    @Json(name = "categoriaId") val categoriaId: Int?,
    @Json(name = "procesador") val procesador: String?,
    @Json(name = "ram") val ram: String?,
    @Json(name = "almacenamiento") val almacenamiento: String?,
    @Json(name = "calificacion") val calificacion: Double?,
    @Json(name = "galeria") val galeria: List<String>?

)