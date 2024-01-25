package com.packt.feature.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packt.feature.chat.domain.usecases.DisconnectMessages
import com.packt.feature.chat.domain.usecases.GetInitialChatRoomInformation
import com.packt.feature.chat.domain.usecases.RetrieveMessages
import com.packt.feature.chat.domain.usecases.SendMessage
import com.packt.feature.chat.ui.model.Chat
import com.packt.feature.chat.ui.model.Message
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
import com.packt.feature.chat.domain.models.Message as DomainMessage

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val retrieveMessages: RetrieveMessages,
    private val sendMessage: SendMessage,
    private val disconnectMessages: DisconnectMessages,
    private val getInitialChatRoomInformation: GetInitialChatRoomInformation
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _uiState = MutableStateFlow(Chat())
    val uiState: StateFlow<Chat> = _uiState

    private var messageCollectionJob: Job? = null

    fun loadChatInformation(id: String) {
        messageCollectionJob = viewModelScope.launch(Dispatchers.IO) {
            val chatRoom = getInitialChatRoomInformation(id)
            withContext(Dispatchers.Main) {
                _uiState.value = chatRoom.toUI()
                _messages.value = chatRoom.lastMessages.map { it.toUI()}
                updateMessages()
            }
        }
    }

    fun updateMessages() {
        messageCollectionJob = viewModelScope.launch(Dispatchers.IO) {
            retrieveMessages()
                .map { it.toUI() }
                .collect { message ->
                withContext(Dispatchers.Main) {
                    _messages.value = _messages.value + message
                }
            }
        }
    }

    private fun DomainMessage.toUI(): Message {
        return Message(
            id = id,
            senderName = senderName,
            senderAvatar = senderAvatar,
            timestamp = timestamp,
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
           // val message = Message(messageText)
           // sendMessage(message)
        }
    }

    override fun onCleared() {
        messageCollectionJob?.cancel()
        viewModelScope.launch(Dispatchers.IO) {
            disconnectMessages()
        }
    }
}
