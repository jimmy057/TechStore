package com.example.techstore.presentacion.Favorite

sealed class FavoriteUiEvent {
    object LoadFavorites : FavoriteUiEvent()
    data class ToggleFavorite(val productoId: Int) : FavoriteUiEvent()
}