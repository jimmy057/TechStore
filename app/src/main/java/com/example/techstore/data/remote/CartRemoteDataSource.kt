package com.example.techstore.data.remote

import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.carrito.CarritoItemDto
import com.example.techstore.data.remote.Dto.carrito.CarritoItems
import retrofit2.Response
import javax.inject.Inject

class CartRemoteDataSource @Inject constructor(
    private val api: TechStoreApi
) {
    suspend fun getCarrito(usuarioId: Int): List<CarritoItems> {
        return api.getCarrito(usuarioId)
    }

    suspend fun agregarAlCarrito(request: CarritoItemDto): Response<Unit> {
        return api.agregarAlCarritoServidor(request)
    }
}