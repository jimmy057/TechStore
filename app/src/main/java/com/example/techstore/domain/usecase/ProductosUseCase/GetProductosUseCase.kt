package com.example.techstore.domain.usecase.ProductosUseCase

import com.example.techstore.domain.model.Producto
import com.example.techstore.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductosUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    operator fun invoke(): Flow<List<Producto>> {
        return repository.getProductos()
    }
}