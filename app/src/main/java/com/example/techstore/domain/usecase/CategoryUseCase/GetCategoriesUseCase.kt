package com.example.techstore.domain.usecase.CategoryUseCase

import com.example.techstore.domain.model.Category
import com.example.techstore.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: CategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> = repository.getCategories()
}