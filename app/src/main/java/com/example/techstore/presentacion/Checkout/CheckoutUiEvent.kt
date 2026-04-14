package com.example.techstore.presentacion.Checkout

sealed class CheckoutUiEvent {
    data class ConfirmarPedido(
        val direccion: String,
        val metodoPago: String
    ) : CheckoutUiEvent()

    object LimpiarError : CheckoutUiEvent()
}