package com.example.techstore.domain.usecase.CategoryUseCase

import com.example.techstore.domain.repository.CategoryRepository
import javax.inject.Inject

class RefreshCategoriesUseCase @Inject constructor(private val repository: CategoryRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshCategories()
}