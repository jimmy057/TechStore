package com.example.techstore.presentacion.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techstore.domain.usecase.LoginUseCase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun onEvent(event: RegisterUIEvent) {
        when (event) {
            is RegisterUIEvent.OnNombreChanged -> _uiState.update { it.copy(nombre = event.nombre) }
            is RegisterUIEvent.OnEmailChanged -> _uiState.update { it.copy(email = event.email) }
            is RegisterUIEvent.OnPasswordChanged -> _uiState.update { it.copy(password = event.password) }
            is RegisterUIEvent.LimpiarError -> _uiState.update { it.copy(errorMessage = null) }
            is RegisterUIEvent.SubmitRegister -> registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val currentState = _uiState.value

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = registerUseCase(
                nombre = currentState.nombre,
                email = currentState.email,
                clave = currentState.password
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                val error = result.exceptionOrNull()?.message ?: "Error al crear la cuenta"
                _uiState.update { it.copy(isLoading = false, errorMessage = error) }
            }
        }
    }
}