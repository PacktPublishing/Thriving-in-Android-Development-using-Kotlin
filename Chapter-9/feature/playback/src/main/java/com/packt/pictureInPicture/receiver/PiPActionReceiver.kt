package com.packt.pictureInPicture.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint

class PiPActionReceiver(private val togglePlayPause: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_TOGGLE_PLAY -> {
                togglePlayPause()
            }
        }
    }

    companion object {
        const val ACTION_TOGGLE_PLAY = "com.packflix.action.TOGGLE_PLAY"
    }
}
