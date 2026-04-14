package com.example.techstore.domain.model

data class UserSession(
    val userId: Int,
    val userName: String,
    val token: String,
    val role: String
)