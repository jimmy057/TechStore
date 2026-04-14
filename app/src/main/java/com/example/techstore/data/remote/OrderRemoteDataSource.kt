package com.example.techstore.data.remote

import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.pedido.PedidoDto
import retrofit2.Response
import javax.inject.Inject

class OrderRemoteDataSource @Inject constructor(
    private val api: TechStoreApi
) {
    suspend fun getPedidos(usuarioId: Int): List<PedidoDto> {
        return api.getPedidos(usuarioId)
    }

    suspend fun crearPedido(request: PedidoDto): Response<Unit> {
        return api.crearPedido(request)
    }
}