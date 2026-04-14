package com.example.techstore.presentacion.Detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techstore.domain.model.CartItem
import com.example.techstore.domain.repository.ProductoRepository
import com.example.techstore.domain.repository.FavoriteRepository
import com.example.techstore.domain.usecase.CarritoUseCase.CartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ProductoRepository,
    private val cartUseCases: CartUseCases,
    private val favoriteRepository: FavoriteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    init {
        savedStateHandle.get<Int>("productId")?.let { id ->
            onEvent(DetailEvent.CargarProducto(id))
        }
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.CargarProducto -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    val favoritosActuales = favoriteRepository.getFavorites().first()
                    val esFavorito = favoritosActuales.any { it.productoId == event.id }

                    repository.getProductoById(event.id).collect { productoEncontrado ->
                        if (productoEncontrado != null) {
                            _state.update {
                                it.copy(
                                    producto = productoEncontrado,
                                    isFavorite = esFavorito,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(isLoading = false, error = "Producto no encontrado")
                            }
                        }
                    }
                }
            }

            is DetailEvent.ToggleFavorito -> {
                val producto = _state.value.producto ?: return
                viewModelScope.launch {
                    val result = favoriteRepository.toggleFavorite(producto.id)
                    if (result.isSuccess) {
                        _state.update { it.copy(isFavorite = !it.isFavorite) }
                    }
                }
            }

            is DetailEvent.AgregarAlCarrito -> {
                val producto = _state.value.producto
                if (producto != null) {
                    viewModelScope.launch {
                        val cartItem = CartItem(
                            productoId = producto.id,
                            nombre = producto.nombre,
                            precio = producto.precioOferta ?: producto.precio,
                            imagenUrl = producto.imagenUrl,
                            cantidad = 1
                        )

                        val result = cartUseCases.addToCart(cartItem)

                        if (result.isSuccess) {
                            _state.update { it.copy(cartMessage = "¡Producto agregado al carrito! 🛒") }
                        } else {
                            _state.update { it.copy(cartMessage = "Error al agregar al carrito") }
                        }
                    }
                }
            }
            is DetailEvent.LimpiarMensajeCarrito -> {
                _state.update { it.copy(cartMessage = null) }
            }
        }
    }
}