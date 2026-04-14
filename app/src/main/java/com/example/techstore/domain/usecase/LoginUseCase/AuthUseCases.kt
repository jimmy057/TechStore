package com.example.techstore.domain.usecase.LoginUseCase

data class AuthUseCases(
    val login: LoginUseCase,
    val register: RegisterUseCase,
    val logout: LogoutUseCase
)