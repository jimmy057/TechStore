package com.example.techstore.data.remote.Dto.pedido

import com.example.techstore.data.remote.Dto.Producto.ProductoDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PedidoDto(
    @Json(name = "id") val id: Int,
    @Json(name = "fecha") val fecha: String?,
    @Json(name = "total") val total: Double,
    @Json(name = "estado") val estado: String?,
    @Json(name = "direccionEnvio") val direccionEnvio: String?,
    @Json(name = "metodoPago") val metodoPago: String?,
    @Json(name = "usuarioId") val usuarioId: Int,
    @Json(name = "detalles") val detalles: List<DetallePedidoDTO>?
)

@JsonClass(generateAdapter = true)
data class DetallePedidoDTO(
    @Json(name = "id") val id: Int,
    @Json(name = "pedidoId") val pedidoId: Int,
    @Json(name = "productoId") val productoId: Int,
    @Json(name = "producto") val producto: ProductoDto?,
    @Json(name = "cantidad") val cantidad: Int,
    @Json(name = "precioUnitario") val precioUnitario: Double
)