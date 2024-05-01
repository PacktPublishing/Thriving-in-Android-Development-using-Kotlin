package com.packt.feature.chat.data.network.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.packt.feature.chat.data.network.model.FirestoreMessageModel
import com.packt.feature.chat.domain.models.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreMessagesDataSource @Inject constructor(
    private val firestore: FirebaseFirestoreProvider
) {

    fun getMessages(chatId: String, userId: String): Flow<Message> = callbackFlow {

        // Get a reference to the messages subcollection inside the specified chat
        val chatRef = firestore.getFirebaseFirestore().collection("chats").document(chatId).collection("messages")

        // Create a query to get the messages ordered by timestamp (ascending)
        val query = chatRef.orderBy("timestamp", Query.Direction.ASCENDING)

        // Add a snapshot listener to the query to listen for real-time updates
        val listenerRegistration = query.addSnapshotListener { snapshot, exception ->
            // If there's an exception, close the Flow with the exception
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            // Convert the snapshot to a list of Message objects
            val messages = snapshot?.documents?.mapNotNull { doc ->
                val message = doc.toObject(FirestoreMessageModel::class.java)
                message?.copy(id = doc.id) // Copy the message with the document ID
            } ?: emptyList()

            val domainMessages = messages.map { it.toDomain(userId) }

            // Send the list of messages to the Flow
            domainMessages.forEach {
                try {
                    trySend(it).isSuccess
                } catch (e: Exception) {
                    close(e)
                }
            }
        }

        // When the Flow is no longer needed, remove the snapshot listener
        awaitClose { listenerRegistration.remove() }
    }

    fun sendMessage(chatId: String, message: Message) {
        val chatRef = firestore.getFirebaseFirestore().collection("chats").document(chatId).collection("messages")
        chatRef.add(FirestoreMessageModel.fromDomain(message))
    }
}
