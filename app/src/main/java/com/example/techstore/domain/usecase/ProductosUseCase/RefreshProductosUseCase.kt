package com.example.techstore.domain.usecase.ProductosUseCase

import com.example.techstore.domain.repository.ProductoRepository
import javax.inject.Inject

class RefreshProductosUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke() {
        repository.refreshProductos()
    }
}