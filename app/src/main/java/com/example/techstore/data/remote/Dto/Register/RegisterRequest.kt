package com.example.techstore.data.remote.Dto.Register

import com.squareup.moshi.Json

data class RegisterRequest(
    val nombre: String,
    val email: String,
    @Json(name = "clave")
    val clave: String
)