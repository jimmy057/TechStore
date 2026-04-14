package com.example.techstore.presentacion.Carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techstore.domain.usecase.CarritoUseCase.CartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartUseCases: CartUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        onEvent(CartUiEvent.CargarCarrito)
    }

    fun onEvent(event: CartUiEvent) {
        when (event) {
            is CartUiEvent.CargarCarrito -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    cartUseCases.getCart().collect { items ->
                        val totalCalculado = items.sumOf { it.precio * it.cantidad }
                        _uiState.update {
                            it.copy(items = items, total = totalCalculado, isLoading = false)
                        }
                    }
                }
            }
            is CartUiEvent.EliminarItem -> {
                viewModelScope.launch {
                    cartUseCases.removeFromCart(event.productoId)
                }
            }
            is CartUiEvent.IncrementarCantidad -> {
                viewModelScope.launch {
                    val item = _uiState.value.items.find { it.productoId == event.productoId }
                    item?.let { cartUseCases.addToCart(it) }
                }
            }
            is CartUiEvent.DecrementarCantidad -> {
                viewModelScope.launch {
                    val item = _uiState.value.items.find { it.productoId == event.productoId }
                    item?.let {
                        if (it.cantidad > 1) {
                            cartUseCases.decrementQuantity(it.productoId)
                        } else {
                            cartUseCases.removeFromCart(it.productoId)
                        }
                    }
                }
            }
            is CartUiEvent.ProcederAlPago -> {
            }

            is CartUiEvent.LimpiarMensaje -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }
}