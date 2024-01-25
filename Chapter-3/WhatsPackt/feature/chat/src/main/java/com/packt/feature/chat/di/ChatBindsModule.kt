package com.packt.feature.chat.di

import com.packt.feature.chat.data.network.repository.ChatRoomRepository
import com.packt.feature.chat.data.network.repository.MessagesRepository
import com.packt.feature.chat.domain.IChatRoomRepository
import com.packt.feature.chat.domain.IMessagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class ChatBindsModule {

    @Binds
    abstract fun bindsMessagesRepository(
        messagesRepository: MessagesRepository
    ): IMessagesRepository

    @Binds
    abstract fun bindsChatRoomRepository(
        chatRoomRepository: ChatRoomRepository
    ): IChatRoomRepository

}
