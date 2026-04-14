package com.example.techstore.data.repository

import com.example.techstore.data.local.dao.ProductoDao
import com.example.techstore.data.local.datastore.SessionDataStore
import com.example.techstore.data.local.mapper.toEntity
import com.example.techstore.data.remote.ProductoRemoteDataSource
import com.example.techstore.domain.model.Producto
import com.example.techstore.domain.repository.ProductoRepository
import com.example.techstore.data.local.mapper.toDomain
import com.example.techstore.data.local.entities.ProductoEntity
import com.example.techstore.data.remote.Dto.Producto.CreateProductoRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductoRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductoRemoteDataSource,
    private val dao: ProductoDao,
    private val sessionDataStore: SessionDataStore
) : ProductoRepository {

    override fun getProductos(): Flow<List<Producto>> = dao.getProductos().map { entities -> entities.map { it.toDomain() } }
    override fun buscarProductos(query: String): Flow<List<Producto>> = dao.buscarProductosLocal(query).map { entities -> entities.map { it.toDomain() } }
    override fun getProductoById(id: Int): Flow<Producto?> = dao.getProductoById(id).map { it?.toDomain() }

    override suspend fun refreshProductos(): Result<Unit> {
        return try {
            syncUnsyncedProductos()

            val response = remoteDataSource.getProductos()
            if (response.isSuccessful && response.body() != null) {
                val dtos = response.body()!!
                val entities = dtos.map { it.toEntity().copy(isSynced = true) }

                dao.clearSyncedProductos()
                dao.insertProductos(entities)

                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al conectar con el servidor"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception("Sin conexión a internet. Mostrando catálogo guardado."))
        }
    }


    override suspend fun updateProducto(id: Int, producto: Producto): Result<Unit> {
        return try {
            val request = CreateProductoRequest(
                id = id,
                nombre = producto.nombre,
                marca = producto.marca,
                descripcion = producto.descripcion,
                precio = producto.precio,
                precioOferta = producto.precioOferta,
                stock = producto.stock,
                imagenUrl = producto.imagenUrl,
                categoriaId = producto.categoriaId,
                procesador = producto.procesador,
                ram = producto.ram,
                almacenamiento = producto.almacenamiento,
                calificacion = producto.calificacion
            )

            val response = remoteDataSource.actualizarProducto(id, request)

            if (response.isSuccessful) {
                val entityUpdate = ProductoEntity(
                    id = id,
                    nombre = producto.nombre,
                    marca = producto.marca ?: "",
                    descripcion = producto.descripcion ?: "",
                    precio = producto.precio,
                    precioOferta = producto.precioOferta,
                    stock = producto.stock,
                    imagenUrl = producto.imagenUrl ?: "",
                    categoria = "",
                    categoriaId = producto.categoriaId,
                    procesador = producto.procesador ?: "N/A",
                    ram = producto.ram ?: "N/A",
                    almacenamiento = producto.almacenamiento ?: "N/A",
                    calificacion = producto.calificacion,
                    galeriaString = "",
                    isSynced = true
                )

                dao.updateProducto(entityUpdate)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al actualizar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun eliminarProducto(id: Int): Result<Unit> {
        return try {
            val response = remoteDataSource.eliminarProducto(id)

            if (response.isSuccessful) {
                dao.deleteProductoById(id)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun crearProducto(
        nombre: String, marca: String, descripcion: String, precio: Double,
        stock: Int, imagenUrl: String, categoriaId: Int,
        procesador: String, ram: String, almacenamiento: String
    ): Result<Unit> {
        return try {
            val entityOffline = ProductoEntity(
                nombre = nombre, marca = marca, descripcion = descripcion,
                precio = precio, precioOferta = null, stock = stock,
                imagenUrl = imagenUrl, categoria = "", categoriaId = categoriaId,
                procesador = procesador, ram = ram, almacenamiento = almacenamiento,
                calificacion = 0.0, galeriaString = "", isSynced = false
            )
            dao.insertProducto(entityOffline)

            syncUnsyncedProductos()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun syncUnsyncedProductos() {
        val pendientes = dao.getUnsyncedProductos()
        for (item in pendientes) {
            try {
                val request = CreateProductoRequest(
                    nombre = item.nombre, marca = item.marca ?: "", descripcion = item.descripcion ?: "",
                    precio = item.precio, stock = item.stock, imagenUrl = item.imagenUrl ?: "",
                    categoriaId = item.categoriaId, procesador = item.procesador ?: "N/A",
                    ram = item.ram ?: "N/A", almacenamiento = item.almacenamiento ?: "N/A"
                )
                val response = remoteDataSource.crearProducto(request)
                if (response.isSuccessful) {
                    dao.markAsSynced(item.id, item.id)
                }
            } catch (e: Exception) {
            }
        }
    }

    override fun getProductosPorCategoria(categoria: String): Flow<List<Producto>> {
        return dao.getProductosPorCategoria(categoria).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}