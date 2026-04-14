package com.example.techstore.domain.usecase.ProductosUseCase

data class ProductoUseCases(
    val getProductos: GetProductosUseCase,
    val getProductoById: GetProductoByIdUseCase,
    val refreshProductos: RefreshProductosUseCase,
    val buscarProductos: BuscarProductosUseCase,
    val createProducto: CreateProductoUseCase
)