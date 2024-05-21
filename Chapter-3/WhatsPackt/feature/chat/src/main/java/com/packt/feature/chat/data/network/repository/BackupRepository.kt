package com.packt.feature.chat.data.network.repository

import com.google.gson.Gson
import com.packt.data.database.ConversationDao
import com.packt.data.database.MessageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class BackupRepository @Inject constructor(
    private val messageDao: MessageDao,
    private val conversationDao: ConversationDao,
    private val storageDataSource: StorageDataSource

) {

    private val gson = Gson()

    suspend fun backupAllConversations() = withContext(Dispatchers.IO) {

        // Get all the conversations
        val conversations = conversationDao.getAllConversations()
        // Backup each conversation
        conversations.collect {
            for (conversation in it) {
                val messages = messageDao.getMessagesInConversation(conversation.id)
                // create a JSON representation of the messages
                val messagesJson = gson.toJson(messages)
                // create a temporary file and write the JSON to it
                val tempFile = createTempFile("messages", ".json")
                tempFile.writeText(messagesJson)
                // upload the file to Firebase Storage
                val remotePath = "conversations/${conversation.id}/messages.json"
                storageDataSource.uploadFile(tempFile, remotePath)
                // delete the local file
                tempFile.delete()

            }
        }
    }


    private fun createTempFile(prefix: String, suffix: String): File {
        // specify the directory where the temporary file will be created
        val tempDir = File(System.getProperty("java.io.tmpdir"))
        // create a temporary file with the specified prefix and suffix
        return File.createTempFile(prefix, suffix, tempDir)

    }

}
