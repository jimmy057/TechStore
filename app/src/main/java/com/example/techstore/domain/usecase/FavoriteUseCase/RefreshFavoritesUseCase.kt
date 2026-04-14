package com.example.techstore.domain.usecase.FavoriteUseCase

import com.example.techstore.domain.repository.FavoriteRepository
import javax.inject.Inject

class RefreshFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.refreshFavorites()
    }
}