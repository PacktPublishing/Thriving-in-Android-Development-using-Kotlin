package com.packt.login.domain

import com.packt.common.domain.LoginRepository

interface DoLoginUseCase {
    suspend fun doLogin(email: String, password: String): Result<Unit>
}

class DoLogin(
    private val loginRepository: LoginRepository
) : DoLoginUseCase {

    override suspend fun doLogin(email: String, password: String): Result<Unit> {
        return loginRepository.loginWithCredentials(email, password)
    }
}