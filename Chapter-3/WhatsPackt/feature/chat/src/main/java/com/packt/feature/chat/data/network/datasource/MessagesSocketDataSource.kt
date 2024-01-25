package com.packt.feature.chat.data.network.datasource

import android.util.Log
import com.packt.feature.chat.data.network.model.WebsocketMessageModel
import com.packt.feature.chat.di.ChatModule.Companion.WEBSOCKET_CLIENT
import com.packt.feature.chat.di.ChatModule.Companion.WEBSOCKET_URL_NAME
import com.packt.feature.chat.domain.models.Message
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.serialization.*
import io.ktor.utils.io.errors.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Named

class MessagesSocketDataSource @Inject constructor(
    @Named(WEBSOCKET_CLIENT) private val httpClient: HttpClient,
    @Named(WEBSOCKET_URL_NAME) private val websocketUrl: String
) {
    companion object {
        const val TAG = "MessagesSocketDataSource"
        const val RETRY_DELAY = 10000L
        const val MAX_RETRIES = 5
    }

    private lateinit var webSocketSession: DefaultClientWebSocketSession

    suspend fun connect(): Flow<Message> {
        return flow {
            // Wrap the connection attempt with a try-catch block
            try {
                httpClient.webSocketSession { url(websocketUrl) }
                    .apply { webSocketSession = this }
                    .incoming
                    .receiveAsFlow()
                    .collect { frame ->
                        try {
                            // Handle errors while processing the message
                            val message = webSocketSession.handleMessage(frame)?.toDomain()
                            if (message != null) {
                                emit(message)
                            }
                        } catch (e: Exception) {
                            // Log or handle the error gracefully
                            Log.e(TAG, "Error handling WebSocket frame", e)
                        }
                    }
            } catch (e: Exception) {
                // Log or handle the connection error gracefully
                Log.e(TAG, "Error connecting to WebSocket", e)
            }
        }.retryWhen { cause, attempt ->
            // Implement a retry strategy based on the cause and/or the number of attempts
            if (cause is IOException && attempt < MAX_RETRIES) {
                delay(RETRY_DELAY)
                true
            } else {
                false
            }
        }.catch { e ->
            // Handle exceptions from the Flow
            Log.e(TAG, "Error in WebSocket Flow", e)
        }
    }

    private suspend fun DefaultClientWebSocketSession.handleMessage(frame: Frame): WebsocketMessageModel? {
        return when (frame) {
            is Frame.Text -> converter?.deserialize(frame)
            is Frame.Close -> {
                disconnect()
                null
            }
            else -> null
        }
    }

    suspend fun sendMessage(message: Message) {
        val websocketMessage = WebsocketMessageModel.fromDomain(message)
        webSocketSession.converter?.serialize(websocketMessage)?.let {
            webSocketSession.send(it)
        }
    }

    suspend fun disconnect() {
        webSocketSession.close(CloseReason(CloseReason.Codes.NORMAL, "Disconnect"))
    }

}
