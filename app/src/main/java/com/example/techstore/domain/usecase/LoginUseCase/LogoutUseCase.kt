package com.example.techstore.domain.usecase.LoginUseCase

import com.example.techstore.data.local.datastore.SessionDataStore
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val sessionDataStore: SessionDataStore
) {
    suspend operator fun invoke() {
        sessionDataStore.clearSession()
    }
}