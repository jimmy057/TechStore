package com.example.techstore.domain.usecase.ProductosUseCase

import com.example.techstore.domain.model.Producto
import com.example.techstore.domain.repository.ProductoRepository
import javax.inject.Inject

class UpdateProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(id: Int, producto: Producto) =
        repository.updateProducto(id, producto)
}