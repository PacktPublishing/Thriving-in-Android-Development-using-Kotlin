package com.packt.domain.fcm

interface IFCMTokenRepository {
    suspend fun getFCMToken(): String
}