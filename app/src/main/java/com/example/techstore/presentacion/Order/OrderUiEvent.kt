package com.example.techstore.presentacion.Order

sealed class OrderUiEvent {
    object CargarHistorial : OrderUiEvent()
    object SincronizarConServidor : OrderUiEvent()
    data class LimpiarError(val message: String? = null) : OrderUiEvent()
}