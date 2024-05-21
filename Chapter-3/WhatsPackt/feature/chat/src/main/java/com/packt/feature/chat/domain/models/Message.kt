package com.packt.feature.chat.domain.models

data class Message(
    val id: String? = null,
    val conversationId: String,
    val senderName: String,
    val senderAvatar: String,
    val timestamp: String? = null,
    val isMine: Boolean,
    val contentType: ContentType,
    val content: String,
    val contentDescription: String
) {
    enum class ContentType {
        TEXT, IMAGE
    }
}