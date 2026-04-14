package com.example.techstore.domain.usecase.FavoriteUseCase

import com.example.techstore.domain.model.Favorite
import com.example.techstore.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<Favorite>> {
        return repository.getFavorites()
    }
}