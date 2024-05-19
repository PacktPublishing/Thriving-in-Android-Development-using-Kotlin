package com.packt.login.data

import com.packt.login.data.model.LoginRequest
import com.packt.login.domain.model.AuthToken
import retrofit2.Response

class LoginRemoteDataSource(
    private val authService: AuthService
) {

    suspend fun login(email: String, password: String): Result<String> {
        return authService.login(
            LoginRequest(
                email = email,
                password = password
            )
        ).run {
            val token = this.body()?.token
            if (this.isSuccessful && token != null) {
                Result.success(token)
            } else {
                Result.failure(getError(this))
            }
        }
    }

    private fun getError(response: Response<AuthToken>): Throwable {
        return when (response.code()) {
            401 -> LoginException.AuthenticationException("Invalid email or password.")
            403 -> LoginException.AccessDeniedException("Access denied.")
            404 -> LoginException.NotFoundException("Login endpoint not found.")
            in 500..599 -> LoginException.ServerException("Server error: ${response.message()}.")
            else -> LoginException.HttpException(
                response.code(),
                "HTTP error: ${response.code()} ${response.message()}."
            )
        }
    }
}

sealed class LoginException(loginErrorMessage: String, val code: Int? = null) : Exception(loginErrorMessage) {
    class AuthenticationException(message: String) : LoginException(message)
    class AccessDeniedException(message: String) : LoginException(message)
    class NotFoundException(message: String) : LoginException(message)
    class ServerException(message: String) : LoginException(message)
    class HttpException(code: Int, message: String) : LoginException(message, code)
}

