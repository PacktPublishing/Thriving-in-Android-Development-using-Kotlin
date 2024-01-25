package com.packt.feature.chat.domain

import com.packt.feature.chat.domain.models.ChatRoom

interface IChatRoomRepository {
    suspend fun getInitialChatRoom(id: String): ChatRoom
}