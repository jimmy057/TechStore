package com.example.techstore.data.remote

import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.Producto.CreateProductoRequest
import com.example.techstore.data.remote.Dto.Producto.ProductoDto
import retrofit2.Response
import javax.inject.Inject

class ProductoRemoteDataSource @Inject constructor(
    private val api: TechStoreApi
) {
    suspend fun getProductos(categoriaId: Int? = null): Response<List<ProductoDto>> {
        return api.getProductos(categoriaId)
    }

    suspend fun buscarProductos(query: String): Response<List<ProductoDto>> {
        return api.buscarProductos(query)
    }

    suspend fun getProductoDetalle(id: Int): Response<ProductoDto> {
        return api.getProductoDetalle(id)
    }

    suspend fun crearProducto(request: CreateProductoRequest): Response<Unit> {
        return api.crearProducto(request)
    }

    suspend fun actualizarProducto(id: Int, request: CreateProductoRequest): Response<Unit> {
        return api.actualizarProducto(id, request)
    }

    suspend fun eliminarProducto(id: Int): Response<Unit> {
        return api.eliminarProducto(id)
    }
}