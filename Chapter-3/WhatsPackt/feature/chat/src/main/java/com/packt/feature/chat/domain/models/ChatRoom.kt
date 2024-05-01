package com.packt.feature.chat.domain.models

data class ChatRoom(
    val id: String,
    val senderName: String,
    val senderAvatar: String,
    val lastMessages: List<Message>
)