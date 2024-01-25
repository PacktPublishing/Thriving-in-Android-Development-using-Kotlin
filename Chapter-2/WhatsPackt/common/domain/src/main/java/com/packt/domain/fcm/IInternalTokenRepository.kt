package com.packt.domain.fcm

interface IInternalTokenRepository {
    suspend fun storeToken(userId: String, token: String)
}
