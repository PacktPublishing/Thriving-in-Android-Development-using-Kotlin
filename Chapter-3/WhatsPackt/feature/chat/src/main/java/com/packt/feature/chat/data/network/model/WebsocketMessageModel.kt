package com.packt.feature.chat.data.network.model

import com.packt.feature.chat.domain.models.Message
import kotlinx.serialization.Serializable

@Serializable
class WebsocketMessageModel(
    val id: String? = null,
    val message: String,
    val senderName: String,
    val senderAvatar: String,
    val timestamp: String? = null,
    val isMine: Boolean,
    val messageType: String,
    val messageDescription: String,
    val conversationId: String,
) {

    companion object {
        const val TYPE_TEXT = "TEXT"
        const val TYPE_IMAGE = "IMAGE"

        fun fromDomain(message: Message): WebsocketMessageModel {
            return WebsocketMessageModel(
                message = message.content,
                senderAvatar = message.senderAvatar,
                senderName = message.senderName,
                isMine = message.isMine,
                messageType = message.fromContentType(),
                messageDescription = message.contentDescription,
                conversationId = message.conversationId
            )
        }
    }

    fun toDomain(): Message {
        return Message(
            id = id,
            content = message,
            senderAvatar = senderAvatar,
            senderName = senderName,
            timestamp = timestamp,
            isMine = isMine,
            contentDescription = messageDescription,
            contentType = toContentType(),
            conversationId = conversationId
        )
    }

    fun toContentType(): Message.ContentType {
        return when(messageType) {
            TYPE_IMAGE -> Message.ContentType.IMAGE
            else -> Message.ContentType.TEXT
        }
    }
}

fun Message.fromContentType(): String {
    return when(contentType) {
        Message.ContentType.IMAGE -> WebsocketMessageModel.TYPE_IMAGE
        else -> WebsocketMessageModel.TYPE_TEXT
    }
}