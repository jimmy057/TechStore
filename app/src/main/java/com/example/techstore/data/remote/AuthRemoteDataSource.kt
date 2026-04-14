package com.example.techstore.data.remote

import com.example.techstore.data.remote.Api.TechStoreApi
import com.example.techstore.data.remote.Dto.Login.AuthResponse
import com.example.techstore.data.remote.Dto.Login.LoginRequest
import com.example.techstore.data.remote.Dto.Register.RegisterRequest
import retrofit2.Response
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val api: TechStoreApi
) {
    suspend fun login(request: LoginRequest): Response<AuthResponse> {
        return api.login(request)
    }

    suspend fun register(request: RegisterRequest): Response<Unit> {
        return api.register(request)
    }
}