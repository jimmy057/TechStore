package com.example.techstore.presentacion.Admin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techstore.domain.model.Producto
import com.example.techstore.domain.repository.ProductoRepository
import com.example.techstore.domain.usecase.ProductosUseCase.CreateProductoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val createProductoUseCase: CreateProductoUseCase,
    private val repository: ProductoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getProductos().collect { lista ->
                _uiState.update { it.copy(productos = lista) }
            }
        }

        val productoId = savedStateHandle.get<String>("productoId")?.toIntOrNull()
        if (productoId != null && productoId != -1) {
            cargarDatosDelProducto(productoId)
        }
    }

    private fun cargarDatosDelProducto(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getProductoById(id).collect { producto ->
                if (producto != null) {
                    onEvent(AdminUiEvent.CargarProductoParaEditar(producto))
                }
            }
        }
    }

    fun onEvent(event: AdminUiEvent) {
        when (event) {
            is AdminUiEvent.CargarProductoParaEditar -> {
                _uiState.update {
                    it.copy(
                        isEditMode = true,
                        productoId = event.producto.id,
                        nombre = event.producto.nombre,
                        marca = event.producto.marca ?: "",
                        descripcion = event.producto.descripcion ?: "",
                        precio = event.producto.precio.toString(),
                        stock = event.producto.stock.toString(),
                        imagenUrl = event.producto.imagenUrl ?: "",
                        categoriaId = event.producto.categoriaId.toString(),
                        procesador = event.producto.procesador ?: "",
                        ram = event.producto.ram ?: "",
                        almacenamiento = event.producto.almacenamiento ?: "",
                        isLoading = false
                    )
                }
            }

            is AdminUiEvent.OnEditClicked -> {
                cargarDatosDelProducto(event.id)
            }

            // ✅ NUEVO: La acción de borrar ahora avisa si tuvo éxito
            is AdminUiEvent.OnDeleteClicked -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = true) }
                    val result = repository.eliminarProducto(event.id)
                    if (result.isSuccess) {
                        _uiState.update {
                            it.copy(isLoading = false, isSuccess = true, successMessage = "¡Producto eliminado correctamente!")
                        }
                    } else {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = "Error al eliminar el producto.")
                        }
                    }
                }
            }

            is AdminUiEvent.OnNombreChanged -> _uiState.update { it.copy(nombre = event.nombre) }
            is AdminUiEvent.OnMarcaChanged -> _uiState.update { it.copy(marca = event.marca) }
            is AdminUiEvent.OnDescripcionChanged -> _uiState.update { it.copy(descripcion = event.descripcion) }
            is AdminUiEvent.OnPrecioChanged -> _uiState.update { it.copy(precio = event.precio) }
            is AdminUiEvent.OnStockChanged -> _uiState.update { it.copy(stock = event.stock) }
            is AdminUiEvent.OnImagenUrlChanged -> _uiState.update { it.copy(imagenUrl = event.url) }
            is AdminUiEvent.OnCategoriaIdChanged -> _uiState.update { it.copy(categoriaId = event.id) }
            is AdminUiEvent.LimpiarError -> _uiState.update { it.copy(errorMessage = null) }
            is AdminUiEvent.ResetSuccess -> _uiState.update { AdminUiState(productos = it.productos) }
            is AdminUiEvent.SubmitProducto -> guardarProducto()
        }
    }

    private fun guardarProducto() {
        val state = _uiState.value

        val nombreClean = state.nombre.trim()
        val marcaClean = state.marca.trim().ifBlank { "Genérica" }
        val descClean = state.descripcion.trim().replace("\"", "").ifBlank { "Sin descripción" }
        val precioDouble = state.precio.trim().toDoubleOrNull()
        val stockInt = state.stock.trim().toIntOrNull()
        val catIdInt = state.categoriaId.trim().toIntOrNull()

        if (nombreClean.isEmpty() || precioDouble == null || stockInt == null || catIdInt == null) {
            _uiState.update { it.copy(errorMessage = "Por favor, completa Nombre, Precio, Stock y Categoría con valores válidos.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            if (state.isEditMode && state.productoId != null) {
                val productoActualizado = Producto(
                    id = state.productoId,
                    nombre = nombreClean, marca = marcaClean, descripcion = descClean,
                    precio = precioDouble, precioOferta = null, stock = stockInt,
                    imagenUrl = state.imagenUrl.trim(), categoria = "", categoriaId = catIdInt,
                    procesador = state.procesador, ram = state.ram, almacenamiento = state.almacenamiento,
                    calificacion = 5.0, galeriaUrls = emptyList()
                )

                val result = repository.updateProducto(state.productoId, productoActualizado)

                if (result.isSuccess) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, successMessage = "¡Producto actualizado exitosamente!") }
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Error al actualizar."
                    _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                }

            } else {
                val result = createProductoUseCase(
                    nombre = nombreClean, marca = marcaClean, descripcion = descClean,
                    precio = precioDouble, stock = stockInt, imagenUrl = state.imagenUrl.trim(),
                    categoriaId = catIdInt, procesador = state.procesador, ram = state.ram, almacenamiento = state.almacenamiento
                )

                if (result.isSuccess) {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, successMessage = "¡Producto guardado exitosamente!") }
                } else {
                    val errorMsg = result.exceptionOrNull()?.message ?: "Error desconocido en el servidor."
                    _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                }
            }
        }
    }
}