package com.example.techstore.presentacion.Carrito

sealed class CartUiEvent {
    object CargarCarrito : CartUiEvent()
    data class EliminarItem(val productoId: Int) : CartUiEvent()
    object ProcederAlPago : CartUiEvent()
    object LimpiarMensaje : CartUiEvent()
    data class IncrementarCantidad(val productoId: Int) : CartUiEvent()
    data class DecrementarCantidad(val productoId: Int) : CartUiEvent()
}