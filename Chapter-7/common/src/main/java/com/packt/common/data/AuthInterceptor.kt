package com.packt.common.data

import com.packt.common.domain.AuthTokenProvider
import com.packt.common.domain.LoginRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authTokenProvider: AuthTokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = authTokenProvider.getToken()
        val requestWithToken = originalRequest.newBuilder()
            .apply {
                if (token != null) {
                    header("Authorization", "Bearer $token")
                }
            }
            .build()
        return chain.proceed(requestWithToken)
    }
}
