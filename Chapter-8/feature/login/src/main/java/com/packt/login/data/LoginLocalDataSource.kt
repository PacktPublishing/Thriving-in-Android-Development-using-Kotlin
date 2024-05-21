package com.packt.login.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first


val Context.dataStore by preferencesDataStore(name = "user_preferences")

class LoginLocalDataSource(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    suspend fun saveAuthToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun getAuthToken(): Result<String> {
        val preferences = context.dataStore.data.first()
        val token = preferences[TOKEN_KEY]
        return if (token != null) {
            Result.success(token)
        } else {
            Result.failure(TokenNotFoundError())
        }
    }
}

class TokenNotFoundError : Throwable("Auth token not found")
