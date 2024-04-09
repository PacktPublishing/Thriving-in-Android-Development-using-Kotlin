package com.packt.playback.presentation

import android.app.PictureInPictureParams
import android.os.Bundle
import android.util.Rational
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import com.packt.pictureInPicture.receiver.PiPActionReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaybackActivity: ComponentActivity() {

    private lateinit var pipActionReceiver: PiPActionReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaybackScreen()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val aspectRatio = Rational(16, 9)
        val params = PictureInPictureParams.Builder()
            .setAspectRatio(aspectRatio)
            .build()
        enterPictureInPictureMode(params)
    }
}