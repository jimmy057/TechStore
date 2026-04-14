package com.example.techstore.data.remote

import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.categoria.CategoriaDTO
import javax.inject.Inject

class CategoryRemoteDataSource @Inject constructor(
    private val api: TechStoreApi
) {
    suspend fun getCategorias(): List<CategoriaDTO> {
        return api.getCategorias()
    }
}