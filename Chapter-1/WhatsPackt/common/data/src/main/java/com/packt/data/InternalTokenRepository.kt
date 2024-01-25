package com.packt.data

import com.packt.domain.fcm.IInternalTokenRepository

class InternalTokenRepository(): IInternalTokenRepository {
    override suspend fun storeToken(userId: String, token: String) {
        // Store in the data source of your choosing
    }
}