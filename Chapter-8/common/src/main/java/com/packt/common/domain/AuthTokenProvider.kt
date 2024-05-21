package com.packt.common.domain

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore by preferencesDataStore(name = "user_preferences")

class AuthTokenProvider(val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    fun getToken() = runBlocking {
        val preferences = context.dataStore.data.first()
        preferences[TOKEN_KEY]
    }
}