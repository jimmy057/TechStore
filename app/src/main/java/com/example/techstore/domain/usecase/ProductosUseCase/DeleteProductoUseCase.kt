package com.example.techstore.domain.usecase.ProductosUseCase

import com.example.techstore.domain.repository.ProductoRepository
import javax.inject.Inject

class DeleteProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(id: Int) =
        repository.eliminarProducto(id)
}