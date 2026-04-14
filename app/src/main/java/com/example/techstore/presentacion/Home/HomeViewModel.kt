package com.example.techstore.presentacion.Home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.domain.repository.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductoRepository,
    private val sessionDataStore: SessionDataStore,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val categoryFilter = savedStateHandle.getStateFlow<String?>("categoryName", null)

    init {
        observarEstadoAdmin()
        observarProductosConFiltro()
        refrescarProductosDesdeApi()
    }

    private fun observarProductosConFiltro() {
        categoryFilter
            .flatMapLatest { categoria ->
                val filtroLimpio = if (categoria == "{categoryName}" || categoria.isNullOrBlank()) null else categoria

                _state.update { it.copy(activeCategory = filtroLimpio, isLoading = true) }

                if (filtroLimpio == null) {
                    repository.getProductos()
                } else {
                    repository.getProductosPorCategoria(filtroLimpio)
                }
            }
            .onEach { lista ->
                _state.update { it.copy(productos = lista, isLoading = false, error = null) }
            }
            .catch { e -> _state.update { it.copy(error = e.message, isLoading = false) } }
            .launchIn(viewModelScope)
    }

    private fun observarEstadoAdmin() {
        viewModelScope.launch {
            sessionDataStore.isAdmin().collect { esAdmin ->
                _state.update { it.copy(isAdmin = esAdmin) }
            }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.RefrescarPantalla -> refrescarProductosDesdeApi()

            is HomeUiEvent.Buscar -> {
                _state.update { it.copy(searchQuery = event.query) }

                observarBuscador(event.query)
            }

            is HomeUiEvent.LimpiarFiltro -> {
                savedStateHandle["categoryName"] = null
                _state.update { it.copy(searchQuery = "", activeCategory = null) }
            }

            is HomeUiEvent.CargarProductosIniciales -> {
                refrescarProductosDesdeApi()
            }

            else -> {}
        }
    }

    private fun refrescarProductosDesdeApi() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                repository.refreshProductos()
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al conectar con el servidor") }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun observarBuscador(query: String) {
        if (query.isBlank()) {
            observarProductosConFiltro()
            return
        }

        repository.buscarProductos(query)
            .onEach { resultados ->
                _state.update { it.copy(productos = resultados) }
            }
            .launchIn(viewModelScope)
    }
}