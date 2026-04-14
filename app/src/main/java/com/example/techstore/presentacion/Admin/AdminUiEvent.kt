package com.example.techstore.presentacion.Admin

import com.example.techstore.domain.model.Producto

sealed class AdminUiEvent {
    data class CargarProductoParaEditar(val producto: Producto) : AdminUiEvent()

    data class OnNombreChanged(val nombre: String) : AdminUiEvent()
    data class OnEditClicked(val id: Int) : AdminUiEvent()
    data class OnDeleteClicked(val id: Int) : AdminUiEvent()
    data class OnMarcaChanged(val marca: String) : AdminUiEvent()
    data class OnDescripcionChanged(val descripcion: String) : AdminUiEvent()
    data class OnPrecioChanged(val precio: String) : AdminUiEvent()
    data class OnStockChanged(val stock: String) : AdminUiEvent()
    data class OnImagenUrlChanged(val url: String) : AdminUiEvent()
    data class OnCategoriaIdChanged(val id: String) : AdminUiEvent()
    object SubmitProducto : AdminUiEvent()
    object LimpiarError : AdminUiEvent()
    object ResetSuccess : AdminUiEvent()
}