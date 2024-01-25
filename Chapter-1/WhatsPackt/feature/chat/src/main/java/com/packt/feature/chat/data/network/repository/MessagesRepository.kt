package com.packt.feature.chat.data.network.repository

import com.packt.feature.chat.data.network.datasource.FirestoreMessagesDataSource
import com.packt.feature.chat.data.network.datasource.MessagesSocketDataSource
import com.packt.feature.chat.domain.IMessagesRepository
import com.packt.feature.chat.domain.models.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    //private val dataSource: MessagesSocketDataSource
    private val dataSource: FirestoreMessagesDataSource
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
}