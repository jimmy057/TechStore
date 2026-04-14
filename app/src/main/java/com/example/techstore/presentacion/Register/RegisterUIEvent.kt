package com.example.techstore.presentacion.Register

sealed class RegisterUIEvent {
    data class OnNombreChanged(val nombre: String) : RegisterUIEvent()
    data class OnEmailChanged(val email: String) : RegisterUIEvent()
    data class OnPasswordChanged(val password: String) : RegisterUIEvent()
    object SubmitRegister : RegisterUIEvent()
    object LimpiarError : RegisterUIEvent()
}