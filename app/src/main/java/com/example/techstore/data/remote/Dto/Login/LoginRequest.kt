package com.example.techstore.data.remote.Dto.Login

import com.squareup.moshi.Json

data class LoginRequest(
    val email: String,
    @Json(name = "clave")
    val clave: String
)