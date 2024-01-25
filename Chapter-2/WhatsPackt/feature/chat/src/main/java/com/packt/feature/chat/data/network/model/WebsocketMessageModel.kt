package com.packt.feature.chat.data.network.model

import com.packt.feature.chat.domain.models.Message
import kotlinx.serialization.Serializable

@Serializable
class WebsocketMessageModel(
    val id: String,
    val message: String,
    val senderName: String,
    val senderAvatar: String,
    val timestamp: String,
    val isMine: Boolean,
    val messageType: String,
    val messageDescription: String
) {

    companion object {
        const val TYPE_TEXT = "TEXT"
        const val TYPE_IMAGE = "IMAGE"

        fun fromDomain(message: Message): WebsocketMessageModel {
            return WebsocketMessageModel(
                id = message.id,
                message = message.content,
                senderAvatar = message.senderAvatar,
                senderName = message.senderName,
                timestamp = message.timestamp,
                isMine = message.isMine,
                messageType = message.fromContentType(),
                messageDescription = message.contentDescription
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
            contentType = toContentType()
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