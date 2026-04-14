package com.example.techstore.domain.repository

import com.example.techstore.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
    suspend fun refreshCategories(): Result<Unit>
}