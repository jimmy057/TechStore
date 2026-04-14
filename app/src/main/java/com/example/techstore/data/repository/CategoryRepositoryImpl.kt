package com.example.techstore.data.repository

import com.example.techstore.data.local.dao.CategoryDao
import com.example.techstore.data.local.mapper.toDomain
import com.example.techstore.data.local.mapper.toEntity
import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.domain.model.Category
import com.example.techstore.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao,
    private val api: TechStoreApi
) : CategoryRepository {

    override fun getCategories(): Flow<List<Category>> =
        dao.getAllCategories().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshCategories(): Result<Unit> {
        return try {
            val response = api.getCategorias()
            val entities = response.map { it.toEntity() }
            dao.clearCategories()
            dao.insertCategories(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}