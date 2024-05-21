package com.packt.feature.chat.data.network.datasource

import com.packt.feature.chat.data.network.model.ChatRoomModel
import com.packt.feature.chat.di.ChatModule.Companion.API_CHAT_ROOM_URL_NAME
import com.packt.feature.chat.di.ChatModule.Companion.API_CLIENT
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject
import javax.inject.Named

class ChatRoomDataSource @Inject constructor(
    @Named(API_CLIENT) private val client: HttpClient,
    @Named(API_CHAT_ROOM_URL_NAME) private val url: String
) {
    suspend fun getInitialChatRoom(id: String): ChatRoomModel {
        return client.get(url.format(id)).body()
    }
}