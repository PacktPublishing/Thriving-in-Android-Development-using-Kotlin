package com.packt.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.packt.framework.navigation.DeepLinks

class WhatsPacktMessagingService: FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "Chat_message"
        const val CHANNEL_DESCRIPTION = "Receive a notification when a chat message is received"
        const val CHANNEL_TITLE = "New chat message notification"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.isNotEmpty()) {

            // We can extract information such as the sender, message content, or chat ID
            val senderName = remoteMessage.data["senderName"]
            val messageContent = remoteMessage.data["message"]
            val chatId = remoteMessage.data["chatId"]
            val messageId = remoteMessage.data["messageId"]

            // Create and show a notification for the received message
            if (chatId != null && messageId != null) {
                showNotification(senderName, messageId, messageContent, chatId)
            }
        }
    }

    private fun showNotification(senderName: String?, messageId: String, messageContent: String?, chatId: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel
        // (if you want to support versions lower than Android Oreo, you will have to check the version here)
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_TITLE,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
        }
        notificationManager.createNotificationChannel(channel)


        // Create an Intent to open the chat when the notification is clicked
        val deepLinkUrl = DeepLinks.chatRoute.replace("{chatId}", chatId)

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUrl)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Create a PendingIntent for the Intent
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Build the notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(senderName)
            .setContentText(messageContent)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager.notify(messageId.toInt(), notification)

    }
}
