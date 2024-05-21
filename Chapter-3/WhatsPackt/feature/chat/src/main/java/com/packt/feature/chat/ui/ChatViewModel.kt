package com.packt.feature.chat.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packt.domain.user.GetUserData
import com.packt.feature.chat.domain.models.ChatRoom
import com.packt.feature.chat.domain.usecases.DisconnectMessages
import com.packt.feature.chat.domain.usecases.GetInitialChatRoomInformation
import com.packt.feature.chat.domain.usecases.RetrieveMessages
import com.packt.feature.chat.domain.usecases.SendMessage
import com.packt.feature.chat.ui.model.Chat
import com.packt.feature.chat.ui.model.Message
import com.packt.feature.chat.domain.models.Message as DomainMessage
import com.packt.feature.chat.ui.model.MessageContent
import com.packt.feature.chat.ui.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val retrieveMessages: RetrieveMessages,
    private val sendMessage: SendMessage,
    private val disconnectMessages: DisconnectMessages,
    private val getInitialChatRoomInformation: GetInitialChatRoomInformation,
    private val getUserData: GetUserData
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _uiState = MutableStateFlow(Chat())
    val uiState: StateFlow<Chat> = _uiState

    private var messageCollectionJob: Job? = null

    private lateinit var chatRoom: ChatRoom

    fun loadChatInformation(id: String) {
        messageCollectionJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                chatRoom = getInitialChatRoomInformation(id)
                withContext(Dispatchers.Main) {
                    _uiState.value = chatRoom.toUI()
                    _messages.value = chatRoom.lastMessages.map { it.toUI()}
                    updateMessages()
                }
            } catch (ie: Throwable) {
                Log.d("TODO", "You can show here a message to the user indicating that an error has happened")

            }
        }
    }

    fun updateMessages() {
        messageCollectionJob = viewModelScope.launch(Dispatchers.IO) {
            try{
                retrieveMessages(userId = getUserData.getData().id, chatId = chatRoom.id)
                    .map { it.toUI() }
                    .collect { message ->
                        withContext(Dispatchers.Main) {
                            _messages.value += message
                        }
                    }
            } catch (ie: Throwable) {
                Log.d("TODO", "You can show here a message to the user indicating that an error has happened")
            }
        }
    }

    private fun DomainMessage.toUI(): Message {
        return Message(
            id = id ?: "",
            senderName = senderName,
            senderAvatar = senderAvatar,
            timestamp = timestamp ?: "",
            isMine = isMine,
            messageContent = getMessageContent()
        )
    }

    private fun DomainMessage.getMessageContent(): MessageContent {
        return when (contentType) {
            DomainMessage.ContentType.TEXT -> MessageContent.TextMessage(content)
            DomainMessage.ContentType.IMAGE -> MessageContent.ImageMessage(content, contentDescription)
        }
    }

    fun onSendMessage(messageText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = getUserData.getData()
            val message = DomainMessage(
                conversationId = chatRoom.id,
                senderAvatar = user.avatar,
                senderName = user.name,
                isMine = true,
                contentType = DomainMessage.ContentType.TEXT,
                content = messageText,
                contentDescription = messageText
            )
            sendMessage(chatId = chatRoom.id, message = message)
        }
    }

    override fun onCleared() {
        messageCollectionJob?.cancel()
        viewModelScope.launch(Dispatchers.IO) {
            disconnectMessages()
        }
    }
}
