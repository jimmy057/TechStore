package com.example.techstore.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SessionDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USER_ID = intPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_TOKEN = stringPreferencesKey("user_token")
        private val USER_ROLE = stringPreferencesKey("user_role")
    }

    suspend fun saveSession(userId: Int, name: String, email: String, token: String, role: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_NAME] = name
            preferences[USER_EMAIL] = email
            preferences[USER_TOKEN] = token
            preferences[USER_ROLE] = role
        }
    }

    fun getUserId(): Flow<Int?> = dataStore.data
        .catch { exception -> if (exception is IOException) emit(emptyPreferences()) else throw exception }
        .map { it[USER_ID] }

    fun getUserName(): Flow<String> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[USER_NAME] ?: "Usuario" }

    fun getUserEmail(): Flow<String> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[USER_EMAIL] ?: "" }

    fun getToken(): Flow<String?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[USER_TOKEN] }

    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map { it[USER_TOKEN] != null }

    fun getUserRole(): Flow<String> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[USER_ROLE] ?: "Cliente" }

    fun isAdmin(): Flow<Boolean> = dataStore.data
        .map { it[USER_ROLE]?.equals("Admin", ignoreCase = true) ?: false }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }
}