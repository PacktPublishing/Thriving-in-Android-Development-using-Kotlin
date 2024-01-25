package com.packt.feature.chat.di

import com.google.firebase.firestore.FirebaseFirestore
import com.packt.feature.chat.data.network.RestClient
import com.packt.feature.chat.data.network.repository.MessagesRepository
import com.packt.feature.chat.data.network.datasource.MessagesSocketDataSource
import com.packt.feature.chat.data.network.WebsocketClient
import com.packt.feature.chat.data.network.repository.ChatRoomRepository
import com.packt.feature.chat.domain.IChatRoomRepository
import com.packt.feature.chat.domain.IMessagesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
abstract class ChatModule {

    companion object {
        const val WEBSOCKET_URL = "ws://whatspackt.com/chat/%s"
        const val WEBSOCKET_URL_NAME = "WEBSOCKET_URL"
        const val WEBSOCKET_CLIENT = "WEBSOCKET_CLIENT"
        const val API_CHAT_ROOM_URL = "http://whatspackt.com/chats/%s"
        const val API_CHAT_ROOM_URL_NAME = "CHATROOM_URL"
        const val API_CLIENT = "API_CLIENT"
    }

    @Provides
    @Named(WEBSOCKET_CLIENT)
    fun providesWebsocketHttpClient(): HttpClient {
        return WebsocketClient.client
    }

    @Provides
    @Named(WEBSOCKET_URL_NAME)
    fun providesWebsocketURL(): String {
        return WEBSOCKET_URL
    }

    @Provides
    @Named(API_CLIENT)
    fun providesAPIHttpClient(): HttpClient {
        return RestClient.client
    }

    @Provides
    @Named(API_CHAT_ROOM_URL_NAME)
    fun providesApiChatUrl(): String {
        return API_CHAT_ROOM_URL
    }

    @Provides
    fun providesFirestoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

}
