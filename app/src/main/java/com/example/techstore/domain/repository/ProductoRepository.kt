package com.example.techstore.domain.repository

import com.example.techstore.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun getProductos(): Flow<List<Producto>>
    fun buscarProductos(query: String): Flow<List<Producto>>
    fun getProductoById(id: Int): Flow<Producto?>
    fun getProductosPorCategoria(categoria: String): Flow<List<Producto>>

    suspend fun refreshProductos(): Result<Unit>
    suspend fun updateProducto(id: Int, producto: Producto): Result<Unit>
    suspend fun eliminarProducto(id: Int): Result<Unit>

    suspend fun crearProducto(
        nombre: String, marca: String, descripcion: String, precio: Double,
        stock: Int, imagenUrl: String, categoriaId: Int,
        procesador: String, ram: String, almacenamiento: String
    ): Result<Unit>
}