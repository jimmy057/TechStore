package com.example.techstore.domain.usecase.FavoriteUseCase

import com.example.techstore.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository
) {
    suspend operator fun invoke(productoId: Int): Result<Unit> = repository.toggleFavorite(productoId)
}