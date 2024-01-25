package com.packt.feature.chat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.packt.feature.chat.ui.model.Message
import com.packt.feature.chat.ui.model.MessageContent
import com.packt.framework.ui.Avatar

@Composable
fun MessageItem(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isMine) {
            Avatar(
                imageUrl = message.senderAvatar,
                size = 40.dp,
                contentDescription = "${message.senderName}'s avatar"
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column {
            if (message.isMine) {
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Text(
                    text = message.senderName,
                    fontWeight = FontWeight.Bold
                )
            }
            when (val content = message.messageContent) {
                is MessageContent.TextMessage -> {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (message.isMine) MaterialTheme.colors.primarySurface else MaterialTheme.colors.secondary
                    ) {
                        Text(
                            text = content.message,
                            modifier = Modifier.padding(8.dp),
                            color = if (message.isMine) MaterialTheme.colors.onPrimary else Color.White
                        )
                    }
                }
                is MessageContent.ImageMessage -> {
                    AsyncImage(
                        model = content.imageUrl,
                        contentDescription = content.contentDescription,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Text(
                text = message.timestamp,
                fontSize = 12.sp
            )
        }
    }
}
