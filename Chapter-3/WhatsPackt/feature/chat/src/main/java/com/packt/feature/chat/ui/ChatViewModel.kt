package com.packt.feature.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packt.feature.chat.domain.models.ChatRoom
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
import java.text.SimpleDateFormat
import java.util.Date
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

    private lateinit var chatId: String
    private lateinit var chatRoom: ChatRoom

    fun loadChatInformation(id: String) {
        chatId = id
        messageCollectionJob = viewModelScope.launch(Dispatchers.IO) {
            chatRoom = getInitialChatRoomInformation(id)
            withContext(Dispatchers.Main) {
                _uiState.value = chatRoom.toUI()
                _messages.value = chatRoom.lastMessages.map { it.toUI()}
                loadAndUpdateMessages()
            }
        }
    }

    private fun loadAndUpdateMessages() {
        messageCollectionJob = viewModelScope.launch(Dispatchers.IO) {
            retrieveMessages(chatId)
                .map { it.toUI() }
                .collect { message ->
                    withContext(Dispatchers.Main) {
                        _messages.value += message
                    }
                }
        }
    }

    fun onSendMessage(messageText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sendMessage(chatId, messageText.toMessage())
        }
    }

    override fun onCleared() {
        messageCollectionJob?.cancel()
        viewModelScope.launch(Dispatchers.IO) {
            disconnectMessages()
        }
    }

    private fun String.toMessage(): DomainMessage {
        return DomainMessage(
            id = chatId,
            senderName = chatRoom.senderName,
            senderAvatar = chatRoom.senderAvatar,
            senderId = chatRoom.senderId,
            timestamp = getCurrentDateTime(),
            isMine = true,
            contentType = DomainMessage.ContentType.TEXT,
            content = this,
            contentDescription = "",
            conversationId = chatRoom.id
        )
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

    private fun getCurrentDateTime(): String {
        return SimpleDateFormat.getDateTimeInstance().format(Date())
    }
}
