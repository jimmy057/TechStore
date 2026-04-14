package com.example.techstore.presentacion.Favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.techstore.domain.usecase.FavoriteUseCase.FavoriteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteUseCases: FavoriteUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(FavoriteUiState())
    val state: StateFlow<FavoriteUiState> = _state.asStateFlow()

    init {
        onEvent(FavoriteUiEvent.LoadFavorites)
    }

    fun onEvent(event: FavoriteUiEvent) {
        when (event) {
            is FavoriteUiEvent.LoadFavorites -> {
                viewModelScope.launch {
                    favoriteUseCases.getFavorites().collect { listaDeFavoritos ->
                        _state.update { it.copy(favorites = listaDeFavoritos) }
                    }
                }
            }
            is FavoriteUiEvent.ToggleFavorite -> {
                viewModelScope.launch {
                    favoriteUseCases.toggleFavorite(event.productoId)
                }
            }
        }
    }
}