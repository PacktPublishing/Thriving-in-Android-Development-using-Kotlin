package com.packt.feature.conversations.ui.model

data class Conversation(
    val id: String,
    val name: String,
    val message: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val avatar: String
)