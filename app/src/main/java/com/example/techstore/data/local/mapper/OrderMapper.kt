package com.example.techstore.data.local.mapper

import com.example.techstore.data.local.entities.OrderDetailEntity
import com.example.techstore.data.local.entities.OrderEntity
import com.example.techstore.data.remote.Dto.pedido.DetallePedidoDTO
import com.example.techstore.data.remote.Dto.pedido.PedidoDto
import com.example.techstore.domain.model.Order
import com.example.techstore.domain.model.OrderDetail

fun PedidoDto.toEntity(): OrderEntity {
    return OrderEntity(
        id = this.id,
        fecha = this.fecha ?: "",
        total = this.total,
        estado = this.estado ?: "Pendiente",
        direccionEnvio = this.direccionEnvio ?: "No especificada",
        metodoPago = this.metodoPago ?: "Efectivo",
        usuarioId = this.usuarioId,
        isSynced = true
    )
}

fun OrderEntity.toDomain(detalles: List<OrderDetail> = emptyList()): Order {
    return Order(
        id = this.id,
        fecha = this.fecha,
        total = this.total,
        estado = this.estado ?: "",
        direccionEnvio = this.direccionEnvio ?: "",
        metodoPago = this.metodoPago ?: "",
        usuarioId = this.usuarioId,
        detalles = detalles
    )
}

fun DetallePedidoDTO.toEntity(): OrderDetailEntity {
    return OrderDetailEntity(
        id = this.id,
        pedidoId = this.pedidoId,
        productoId = this.productoId,
        nombreProducto = this.producto?.nombre ?: "Producto desconocido",
        cantidad = this.cantidad,
        precioUnitario = this.precioUnitario
    )
}

fun OrderDetailEntity.toDomain(): OrderDetail {
    return OrderDetail(
        id = this.id,
        pedidoId = this.pedidoId,
        productoId = this.productoId,
        nombreProducto = this.nombreProducto ?: "Producto",
        cantidad = this.cantidad,
        precioUnitario = this.precioUnitario
    )
}