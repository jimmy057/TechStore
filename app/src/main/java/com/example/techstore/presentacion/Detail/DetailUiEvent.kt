package com.example.techstore.presentacion.Detail

sealed class DetailEvent {
    data class CargarProducto(val id: Int) : DetailEvent()
    object AgregarAlCarrito : DetailEvent()
    object LimpiarMensajeCarrito : DetailEvent()
    object ToggleFavorito : DetailEvent()
}