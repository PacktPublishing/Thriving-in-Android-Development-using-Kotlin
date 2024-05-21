package com.packt.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = Conversation::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("conversation_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index(value = ["conversation_id"])
    ]

)
data class Message(

    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,

    @ColumnInfo(name = "conversation_id") val conversationId: String,
    @ColumnInfo(name = "sender_avatar") val senderAvatar: String,
    @ColumnInfo(name = "sender_name") val senderName: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "is_mine") val isMine: Boolean

)