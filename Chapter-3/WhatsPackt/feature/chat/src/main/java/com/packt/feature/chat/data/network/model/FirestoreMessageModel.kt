package com.packt.feature.chat.data.network.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.packt.feature.chat.domain.models.Message
import java.text.SimpleDateFormat
import java.util.*

data class FirestoreMessageModel(

    @Transient
    val id: String = "",

    @get:PropertyName("senderId")
    @set:PropertyName("senderId")
    var senderId: String = "",

    @get:PropertyName("senderName")
    @set:PropertyName("senderName")
    var senderName: String = "",

    @get:PropertyName("senderAvatar")
    @set:PropertyName("senderAvatar")
    var senderAvatar: String = "",

    @get:PropertyName("content")
    @set:PropertyName("content")
    var content: String = "",

    @get:PropertyName("timestamp")
    @set:PropertyName("timestamp")
    var timestamp: Timestamp = Timestamp.now()
) {
    companion object {
        fun fromDomain(message: Message): FirestoreMessageModel {
            return FirestoreMessageModel(
                id = "",
                senderName = message.senderName,
                senderAvatar = message.senderAvatar,
                content = message.content
            )
        }
    }

    fun toDomain(userId: String): Message {
        return Message(
            id = id,
            senderName = senderName,
            senderAvatar = senderAvatar,
            isMine = userId == senderId,
            contentType = Message.ContentType.TEXT,
            content = content,
            contentDescription = "",
            timestamp = timestamp.toDateString(),
            conversationId = ""
        )
    }

    private fun Timestamp.toDateString(): String {
        // Create a SimpleDateFormat instance with the desired format and the default Locale
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        // Convert the Timestamp to a Date object
        val date = toDate()

        // Format the Date object using the SimpleDateFormat instance
        return formatter.format(date)
    }
}
