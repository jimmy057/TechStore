package com.example.techstore.data.remote.Dto.carrito

import com.example.techstore.data.remote.Dto.Producto.ProductoDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CarritoItems(
    @Json(name = "id") val id: Int,
    @Json(name = "usuarioId") val usuarioId: Int,
    @Json(name = "productoId") val productoId: Int,
    @Json(name = "cantidad") val cantidad: Int,
    @Json(name = "producto") val producto: ProductoDto?
)