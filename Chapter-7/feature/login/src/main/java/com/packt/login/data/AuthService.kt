package com.packt.login.data

import com.packt.login.data.model.LoginRequest
import com.packt.login.domain.model.AuthToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthToken>

}