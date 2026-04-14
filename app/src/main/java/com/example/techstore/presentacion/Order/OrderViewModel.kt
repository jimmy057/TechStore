package com.example.techstore.presentacion.Order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techstore.domain.model.Order
import com.example.techstore.domain.usecase.OrderUseCases.OrderUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderUseCases: OrderUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(OrderUiState())
    val state: StateFlow<OrderUiState> = _state.asStateFlow()

    init {
        onEvent(OrderUiEvent.CargarHistorial)
        onEvent(OrderUiEvent.SincronizarConServidor)
    }

    fun onEvent(event: OrderUiEvent) {
        when (event) {
            is OrderUiEvent.CargarHistorial -> cargarPedidosLocales()
            is OrderUiEvent.SincronizarConServidor -> sincronizarConServidor()
            is OrderUiEvent.LimpiarError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun cargarPedidosLocales() {
        viewModelScope.launch {
            orderUseCases.getOrderHistory().collect { listaPedidos ->
                _state.update { it.copy(orders = listaPedidos) }
            }
        }
    }

    private fun sincronizarConServidor() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            orderUseCases.refreshOrderHistory()
                .onSuccess {
                    _state.update { it.copy(isLoading = false, error = null) }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Error al actualizar pedidos"
                        )
                    }
                }
        }
    }

    fun getOrderById(orderId: Int): Order? {
        return _state.value.orders.find { it.id == orderId }
    }
}