package com.packt.feature.chat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.packt.feature.chat.ui.model.Message
import com.packt.feature.chat.ui.model.MessageContent
import com.packt.framework.R

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    chatId: String?,
    onBack: () -> Unit
) {
    val messages by viewModel.messages.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadChatInformation(chatId.orEmpty())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.chat_title, uiState.name.orEmpty()))
                }
            )
        },
        bottomBar = {
            SendMessageBox { viewModel.onSendMessage(it) }
        }
    ) { paddingValues->
        ListOfMessages(paddingValues = paddingValues, messages = messages)
    }

}

@Composable
fun SendMessageBox(sendMessage: (String)->Unit) {
    Box(modifier = Modifier
        .defaultMinSize()
        .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        .fillMaxWidth()
    ) {

        var text by remember { mutableStateOf("") }

        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.CenterStart)
                .height(56.dp),
        )

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(56.dp),
            onClick = {
                sendMessage(text)
                text = ""
            }
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                tint = MaterialTheme.colors.primary,
                contentDescription = "Send message"
            )
        }
    }
}

@Composable
fun ListOfMessages(messages: List<Message>, paddingValues: PaddingValues) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(messages) { message ->
                    MessageItem(message = message)
                }
            }
        }
    }
}

fun getFakeMessages(): List<Message> {
    return listOf(
        Message(
            id = "1",
            senderName = "Alice",
            senderAvatar = "https://i.pravatar.cc/300?img=1",
            isMine = false,
            timestamp = "10:00",
            messageContent = MessageContent.TextMessage(
                message = "Hi, how are you?"
            )
        ),
        Message(
            id = "2",
            senderName = "Lucy",
            senderAvatar = "https://i.pravatar.cc/300?img=2",
            isMine = true,
            timestamp = "10:01",
            messageContent = MessageContent.TextMessage(
                message = "I'm good, thank you! And you?"
            )
        ),
        Message(
            id = "3",
            senderName = "Alice",
            senderAvatar = "https://i.pravatar.cc/300?img=1",
            isMine = false,
            timestamp = "10:02",
            messageContent = MessageContent.TextMessage(
                message = "Super fine!"
            )
        ),
        Message(
            id = "4",
            senderName = "Lucy",
            senderAvatar = "https://i.pravatar.cc/300?img=1",
            isMine = true,
            timestamp = "10:02",
            messageContent = MessageContent.TextMessage(
                message = "Are you going to the Kotlin conference?"
            )
        ),
        Message(
            id = "5",
            senderName = "Alice",
            senderAvatar = "https://i.pravatar.cc/300?img=1",
            isMine = false,
            timestamp = "10:03",
            messageContent = MessageContent.TextMessage(
                message = "Of course! I hope to see you there!"
            )
        ),
        Message(
            id = "5",
            senderName = "Alice",
            senderAvatar = "https://i.pravatar.cc/300?img=1",
            isMine = false,
            timestamp = "10:03",
            messageContent = MessageContent.TextMessage(
                message = "I'm going to arrive pretty early"
            )
        ),
        Message(
            id = "5",
            senderName = "Alice",
            senderAvatar = "https://i.pravatar.cc/300?img=1",
            isMine = false,
            timestamp = "10:03",
            messageContent = MessageContent.TextMessage(
                message = "So maybe we can have a coffee together"
            )
        ),
        Message(
            id = "5",
            senderName = "Alice",
            senderAvatar = "https://i.pravatar.cc/300?img=1",
            isMine = false,
            timestamp = "10:03",
            messageContent = MessageContent.TextMessage(
                message = "Wdyt?"
            )
        ),
    )
}