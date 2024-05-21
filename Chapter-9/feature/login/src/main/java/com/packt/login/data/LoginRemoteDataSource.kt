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
            401 -> AuthenticationException("Invalid email or password.")
            403 -> AccessDeniedException("Access denied.")
            404 -> NotFoundException("Login endpoint not found.")
            in 500..599 -> ServerException("Server error: ${response.message()}.")
            else -> HttpException(response.code(), "HTTP error: ${response.code()} ${response.message()}.")
        }
    }
}

class AuthenticationException(message: String) : Exception(message)
class AccessDeniedException(message: String) : Exception(message)
class NotFoundException(message: String) : Exception(message)
class ServerException(message: String) : Exception(message)
class HttpException(val code: Int, message: String) : Exception(message)
