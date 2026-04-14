package com.example.techstore.presentacion.Home

sealed class HomeUiEvent {
    object CargarProductosIniciales : HomeUiEvent()
    object RefrescarPantalla : HomeUiEvent()
    data class Buscar(val query: String) : HomeUiEvent()
    object LimpiarFiltro : HomeUiEvent()
    data class ProductoSeleccionado(val id: Int) : HomeUiEvent()
}