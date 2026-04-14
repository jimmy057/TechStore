package com.example.techstore.presentacion.Login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.Login.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: TechStoreApi,
    private val sessionDataStore: SessionDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.OnEmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
            }
            is LoginUIEvent.OnPasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
            }
            is LoginUIEvent.SubmitLogin -> {
                realizarLogin()
            }
            is LoginUIEvent.LimpiarError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun realizarLogin() {
        val email = uiState.value.email
        val password = uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor, completa todos los campos.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val response = api.login(LoginRequest(email, password))

                if (response.isSuccessful && response.body() != null) {
                    val authData = response.body()!!

                    sessionDataStore.saveSession(
                        userId = authData.usuarioId,
                        name = authData.nombre,
                        email = authData.email,
                        token = authData.token,
                        role = authData.rol
                    )

                    _uiState.update {
                        it.copy(isLoading = false, isLoginSuccessful = true)
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Correo o contraseña incorrectos.")
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Error de conexión: ${e.localizedMessage}")
                }
            }
        }
    }
}