package com.packt.feature.chat.domain

import com.packt.feature.chat.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface IMessagesRepository {
    suspend fun getMessages(chatId: String, userId: String): Flow<Message>

    suspend fun sendMessage(chatId: String, message: Message)

    suspend fun disconnect()
}