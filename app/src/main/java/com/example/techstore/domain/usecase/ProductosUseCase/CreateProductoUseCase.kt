package com.example.techstore.domain.usecase.ProductosUseCase

import com.example.techstore.domain.repository.ProductoRepository
import javax.inject.Inject

class CreateProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(
        nombre: String,
        marca: String,
        descripcion: String,
        precio: Double,
        stock: Int,
        imagenUrl: String,
        categoriaId: Int,
        procesador: String = "N/A",
        ram: String = "N/A",
        almacenamiento: String = "N/A"
    ): Result<Unit> {
        if (nombre.isBlank() || precio <= 0.0 || stock < 0) {
            return Result.failure(Exception("Nombre, precio y stock son obligatorios y deben ser válidos"))
        }

        return repository.crearProducto(
            nombre, marca, descripcion, precio, stock, imagenUrl, categoriaId,
            procesador, ram, almacenamiento
        )
    }
}