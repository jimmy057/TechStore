package com.example.techstore.data.remote.Dto.Login

import com.squareup.moshi.Json

data class AuthResponse(
    @Json(name = "usuarioId")
    val usuarioId: Int,

    @Json(name = "nombre")
    val nombre: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "token")
    val token: String,
    val rol: String
)