package com.example.techstore.data.remote.Api

import com.example.techstore.data.remote.Dto.Favorito.FavoritoDto
import com.example.techstore.data.remote.Dto.Login.AuthResponse
import com.example.techstore.data.remote.Dto.Login.LoginRequest
import com.example.techstore.data.remote.Dto.Producto.ProductoDto
import com.example.techstore.data.remote.Dto.Register.RegisterRequest
import com.example.techstore.data.remote.Dto.Producto.CreateProductoRequest
import com.example.techstore.data.remote.Dto.carrito.CarritoItemDto
import com.example.techstore.data.remote.Dto.carrito.CarritoItems
import com.example.techstore.data.remote.Dto.categoria.CategoriaDTO
import com.example.techstore.data.remote.Dto.pedido.PedidoDto
import retrofit2.Response
import retrofit2.http.*

interface TechStoreApi {

    // --- PRODUCTOS ---
    @GET("api/Productos")
    suspend fun getProductos(
        @Query("categoriaId") categoriaId: Int? = null
    ): Response<List<ProductoDto>>

    @GET("api/Productos/buscar")
    suspend fun buscarProductos(
        @Query("q") query: String
    ): Response<List<ProductoDto>>

    @GET("api/Productos/{id}")
    suspend fun getProductoDetalle(
        @Path("id") id: Int
    ): Response<ProductoDto>

    @POST("api/Productos")
    suspend fun crearProducto(
        @Body request: CreateProductoRequest
    ): Response<Unit>

    @PUT("api/Productos/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: Int,
        @Body request: CreateProductoRequest
    ): Response<Unit>

    @DELETE("api/Productos/{id}")
    suspend fun eliminarProducto(
        @Path("id") id: Int
    ): Response<Unit>

    // --- CATEGORÍAS ---
    @GET("api/Categorias")
    suspend fun getCategorias(): List<CategoriaDTO>

    // --- AUTENTICACIÓN ---
    @POST("api/Auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/Auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<Unit>

    // --- CARRITO ---
    @GET("api/Carrito/{usuarioId}")
    suspend fun getCarrito(
        @Path("usuarioId") usuarioId: Int
    ): List<CarritoItems>

    @POST("api/Carrito/agregar")
    suspend fun agregarAlCarritoServidor(
        @Body request: CarritoItemDto
    ): Response<Unit>

    @DELETE("api/Carrito/eliminar/{id}")
    suspend fun eliminarItemCarrito(
        @Path("id") id: Int
    ): Response<Unit>

    @DELETE("api/Carrito/vaciar/{usuarioId}")
    suspend fun vaciarCarrito(
        @Path("usuarioId") usuarioId: Int
    ): Response<Unit>

    // --- FAVORITOS ---
    @GET("api/Favoritos/{usuarioId}")
    suspend fun getFavoritos(
        @Path("usuarioId") usuarioId: Int
    ): List<FavoritoDto>

    @POST("api/Favoritos")
    suspend fun agregarFavorito(
        @Body request: FavoritoDto
    ): Response<Unit>

    @DELETE("api/Favoritos/{usuarioId}/{productoId}")
    suspend fun eliminarFavorito(
        @Path("usuarioId") usuarioId: Int,
        @Path("productoId") productoId: Int
    ): Response<Unit>

    // --- PEDIDOS ---
    @GET("api/Pedidos/usuario/{usuarioId}")
    suspend fun getPedidos(
        @Path("usuarioId") usuarioId: Int
    ): List<PedidoDto>

    @POST("api/Pedidos")
    suspend fun crearPedido(
        @Body request: PedidoDto
    ): Response<Unit>
}