package com.packt.playback.presentation

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.PictureInPictureUiState
import android.app.RemoteAction
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.media3.common.util.UnstableApi
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.packt.pictureInPicture.receiver.PiPActionReceiver
import com.packt.playback.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaybackActivity: AppCompatActivity() {

    private lateinit var pipActionReceiver: PiPActionReceiver
    private lateinit var castContext: CastContext
    private val viewModel: PlaybackViewModel by viewModels()
    private var castSession: CastSession? = null

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode)
    }

    override fun onPictureInPictureUiStateChanged(pipState: PictureInPictureUiState) {
        super.onPictureInPictureUiStateChanged(pipState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        castContext = CastContext.getSharedInstance(this)

        pipActionReceiver = PiPActionReceiver {
            viewModel.togglePlayPause()
        }

        val filter = IntentFilter(PiPActionReceiver.ACTION_TOGGLE_PLAY)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(pipActionReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(pipActionReceiver, filter)
        }

        setContent {
            MaterialTheme {
                PlaybackScreen()
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun onStart() {
        super.onStart()
        viewModel.setupPlayer(this)
        castContext.sessionManager.addSessionManagerListener(sessionManagerListener, CastSession::class.java)
    }

    override fun onStop() {
        super.onStop()
        castContext.sessionManager.removeSessionManagerListener(sessionManagerListener, CastSession::class.java)
    }

    override fun onDestroy() {
        unregisterReceiver(pipActionReceiver)
        super.onDestroy()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        val aspectRatio = Rational(16, 9)
        val params = PictureInPictureParams.Builder()
            .setAspectRatio(aspectRatio)
            .setActions(listOf(getIntentForTogglePlayPauseAction()))
            .build()
        enterPictureInPictureMode(params)
    }

    private fun getIntentForTogglePlayPauseAction(): RemoteAction {
        val icon: Icon = Icon.createWithResource(this, R.drawable.baseline_play_arrow_24)
        val intent =  Intent(PiPActionReceiver.ACTION_TOGGLE_PLAY).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        return RemoteAction(icon, "Toggle Play", "Play or Pause the video", intent)
    }

    private fun updateUIForCastSession(isCasting: Boolean) {
        viewModel.setCastingState(isCasting)
    }

    private val sessionManagerListener = object : SessionManagerListener<CastSession> {
        override fun onSessionStarted(session: CastSession, sessionId: String) {
            castSession = session
            updateUIForCastSession(true)
            loadMedia(session)
        }

        override fun onSessionEnded(p0: CastSession, p1: Int) {
            castSession = null
            updateUIForCastSession(false)
        }

        override fun onSessionResumed(session: CastSession, p1: Boolean) {
            castSession = session
            updateUIForCastSession(true)
        }

        override fun onSessionStarting(p0: CastSession) {}

        override fun onSessionStartFailed(p0: CastSession, p1: Int) {}

        override fun onSessionResuming(session: CastSession, p1: String) {}

        override fun onSessionResumeFailed(session: CastSession, p1: Int) { }

        override fun onSessionEnding(session: CastSession) {}

        override fun onSessionSuspended(p0: CastSession, p1: Int) {}
    }

    private fun loadMedia(castSession: CastSession) {
        val mediaInfo = MediaInfo.Builder(viewModel.mediaUrl)
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType("video/mp4")
            .build()
        val mediaLoadOptions = MediaLoadOptions
            .Builder()
            .setAutoplay(true)
            .setPlayPosition(0)
            .build()
        castSession.remoteMediaClient?.load(mediaInfo, mediaLoadOptions)
    }

}