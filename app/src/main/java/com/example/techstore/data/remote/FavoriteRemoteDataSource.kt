package com.example.techstore.data.remote

import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.Favorito.FavoritoDto
import retrofit2.Response
import javax.inject.Inject

class FavoriteRemoteDataSource @Inject constructor(
    private val api: TechStoreApi
) {
    suspend fun getFavoritos(usuarioId: Int): List<FavoritoDto> {
        return api.getFavoritos(usuarioId)
    }

    suspend fun agregarFavorito(request: FavoritoDto): Response<Unit> {
        return api.agregarFavorito(request)
    }

    suspend fun eliminarFavorito(usuarioId: Int, productoId: Int): Response<Unit> {
        return api.eliminarFavorito(usuarioId, productoId)
    }
}