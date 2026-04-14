package com.example.techstore.data.remote.Dto.categoria

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoriaDTO(
    @Json(name = "id") val id: Int,
    @Json(name = "nombre") val nombre: String?,
    @Json(name = "imagenIconoUrl") val imagenIconoUrl: String?
)