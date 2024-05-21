package com.packt.feature.chat.data.network.repository

import com.packt.feature.chat.data.network.datasource.FirestoreMessagesDataSource
import com.packt.feature.chat.data.network.datasource.MessagesLocalDataSource
import com.packt.feature.chat.domain.IMessagesRepository
import com.packt.feature.chat.domain.models.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    //private val dataSource: MessagesSocketDataSource
    private val dataSource: FirestoreMessagesDataSource,
    private val localDataSource: MessagesLocalDataSource
): IMessagesRepository {

    override suspend fun getMessages(chatId: String, userId: String): Flow<Message> {
        return dataSource.getMessages(chatId, userId)
    }

    override suspend fun sendMessage(chatId: String, message: Message) {
        dataSource.sendMessage(chatId, message)
    }

    override suspend fun disconnect() {
        //do nothing, Firestore data source is disconnected as soon as the flow has no subscribers
    }

    /**

    override suspend fun getMessages(chatId: String, userId: String): Flow<Message> {
        return flow {
            try {
                dataSource.connect().collect { message ->
                    localDataSource.insertMessage(message)
                    emit(message)
                    manageDatabaseSize(chatId)
                }
            } catch (e: Exception) {
                localDataSource.getMessagesInConversation(chatId.toInt()).collect {
                    it.forEach { message -> emit(message) }
                }
            }
        }
    }

    override suspend fun sendMessage(chatId: String, message: Message) {
        dataSource.sendMessage(message)
        localDataSource.insertMessage(message)
    }

    override suspend fun disconnect() {
        dataSource.disconnect()
    }

    private suspend fun manageDatabaseSize(chatId: String) {
        val messages = localDataSource.getMessagesInConversation(chatId.toInt()).first()
        if (messages.size > 100) {
            // Delete the oldest messages until we have 100 left
            messages.sortedBy { it.timestamp }.take(messages.size - 100).forEach {
                localDataSource.deleteMessage(it)
            }

        }
    }**/
}
