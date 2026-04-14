package com.example.techstore.presentacion.Checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.domain.usecase.CarritoUseCase.CartUseCases
import com.example.techstore.domain.usecase.OrderUseCases.OrderUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartUseCases: CartUseCases,
    private val orderUseCases: OrderUseCases,
    private val sessionDataStore: SessionDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutUiState())
    val state: StateFlow<CheckoutUiState> = _state.asStateFlow()

    init {
        cargarResumenCarrito()
    }

    private fun cargarResumenCarrito() {
        viewModelScope.launch {
            cartUseCases.getCart().collect { items ->
                val total = items.sumOf { it.precio * it.cantidad }
                _state.update { it.copy(items = items, total = total) }
            }
        }
    }

    fun onEvent(event: CheckoutUiEvent) {
        when (event) {
            is CheckoutUiEvent.ConfirmarPedido -> {
                ejecutarPedido(event.direccion, event.metodoPago)
            }
            is CheckoutUiEvent.LimpiarError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun ejecutarPedido(direccion: String, metodoPago: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val userId = sessionDataStore.getUserId().first()
            val currentItems = _state.value.items
            val total = _state.value.total

            if (userId == null) {
                _state.update { it.copy(isLoading = false, error = "Sesión no válida. Inicia sesión de nuevo.") }
                return@launch
            }

            if (currentItems.isEmpty()) {
                _state.update { it.copy(isLoading = false, error = "El carrito está vacío.") }
                return@launch
            }

            val result = orderUseCases.createOrder(currentItems, total, userId)

            if (result.isSuccess) {
                _state.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Error al procesar el pedido"
                    )
                }
            }
        }
    }
}