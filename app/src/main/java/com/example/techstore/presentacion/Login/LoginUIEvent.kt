package com.example.techstore.presentacion.Login

sealed class LoginUIEvent {
    data class OnEmailChanged(val email: String) : LoginUIEvent()
    data class OnPasswordChanged(val password: String) : LoginUIEvent()
    object SubmitLogin : LoginUIEvent()
    object LimpiarError : LoginUIEvent()
}