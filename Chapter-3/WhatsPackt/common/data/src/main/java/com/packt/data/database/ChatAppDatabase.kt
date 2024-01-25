package com.packt.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Message::class, Conversation::class], version = 1)
abstract class ChatAppDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    abstract fun conversationDao(): ConversationDao

    companion object {

        @Volatile
        private var INSTANCE: ChatAppDatabase? = null

        fun getDatabase(context: Context): ChatAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatAppDatabase::class.java,
                    "chat_database"
                ).build()
                INSTANCE = instance
                instance

            }

        }

    }

}