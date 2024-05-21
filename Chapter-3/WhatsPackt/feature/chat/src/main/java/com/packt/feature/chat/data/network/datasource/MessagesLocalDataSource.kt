package com.packt.feature.chat.data.network.datasource

import com.packt.data.database.Message
import com.packt.feature.chat.domain.models.Message as DomainMessage
import com.packt.data.database.MessageDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessagesLocalDataSource @Inject constructor(private val messageDao: MessageDao) {

    fun getMessagesInConversation(conversationId: Int): Flow<List<DomainMessage>> {
        return messageDao
            .getMessagesInConversation(conversationId.toString())
            .map { messagesList-> messagesList
                .map { message -> mapToDomain(message = message)
                }
            }
    }

    suspend fun insertMessage(message: DomainMessage): Long {
        return messageDao.insertMessage(mapFromDomain(message))
    }

    suspend fun deleteMessage(message: DomainMessage) {
        messageDao.deleteMessage(mapFromDomain(message))
    }

    private fun mapFromDomain(message: DomainMessage): Message {
        return Message(
            id = message.id ?: "",
            conversationId = message.conversationId,
            content = message.content,
            // This is a simplification, in reality
            // there should be a conversion between the date format String to set it as Long
            timestamp = message.timestamp?.toLong() ?: 0,
            senderName = message.senderName,
            senderAvatar = message.senderAvatar,
            isMine = message.isMine
        )
    }

    private fun mapToDomain(message: Message): DomainMessage {
        return DomainMessage(
            id = message.id.toString(),
            conversationId = message.conversationId,
            senderAvatar = message.senderAvatar,
            senderName = message.senderName,
            timestamp = message.timestamp.toString(),
            isMine = message.isMine,
            content = message.content,
            contentType = DomainMessage.ContentType.TEXT,
            contentDescription = ""
        )
    }
}