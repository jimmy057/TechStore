package com.example.techstore.domain.usecase.FavoriteUseCase

data class FavoriteUseCases(
    val getFavorites: GetFavoritesUseCase,
    val toggleFavorite: ToggleFavoriteUseCase,
    val refreshFavorites: RefreshFavoritesUseCase
)