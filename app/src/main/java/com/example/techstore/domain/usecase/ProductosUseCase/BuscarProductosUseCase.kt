package com.example.techstore.domain.usecase.ProductosUseCase

import com.example.techstore.domain.model.Producto
import com.example.techstore.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BuscarProductosUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    operator fun invoke(query: String): Flow<List<Producto>> {
        if (query.isBlank()) {
            return repository.getProductos()
        }
        return repository.buscarProductos(query)
    }
}