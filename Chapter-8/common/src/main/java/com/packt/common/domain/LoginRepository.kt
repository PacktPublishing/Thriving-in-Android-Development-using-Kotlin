package com.packt.common.domain

interface LoginRepository {
    suspend fun getToken(): Result<String>
    suspend fun loginWithCredentials(email: String, password: String): Result<Unit>
}