package com.packt.playback.presentation

import android.content.Context
import android.media.AudioMetadata
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.source.SingleSampleMediaSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(): ViewModel() {

    private val _currentPosition = MutableStateFlow<Long>(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    private val _duration = MutableStateFlow<Long>(0L)
    val duration: MutableStateFlow<Long> = _duration

    private val _isPlaying = MutableStateFlow<Boolean>(false)
    val isPlaying: MutableStateFlow<Boolean> = _isPlaying

    private val _isCasting = MutableStateFlow<Boolean>(false)
    val isCasting: MutableStateFlow<Boolean> = _isCasting

    private var progressUpdateJob: Job? = null

    lateinit var player: ExoPlayer

    var mediaUrl = "https://download.samplelib.com/mp4/sample-30s.mp4"

    @OptIn(UnstableApi::class)
    private fun preparePlayerWithMediaSource(exoPlayer: ExoPlayer) {

        val subtitleUrl = "https://gist.githubusercontent.com/matibzurovski/d690d5c14acbaa399e7f0829f9d6888e/raw/63578ca30e7430be1fa4942d4d8dd599f78151c7/example.srt"

        val videoMediaSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(mediaUrl))

        val subtitleSource = MediaItem.SubtitleConfiguration.Builder(Uri.parse(subtitleUrl))
            .setMimeType(MimeTypes.APPLICATION_SUBRIP)
            .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
            .build()

        val subtitleMediaSource = SingleSampleMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(subtitleSource, C.TIME_UNSET)

        val mergedSource = MergingMediaSource(videoMediaSource, subtitleMediaSource)
        exoPlayer.setMediaSource(mergedSource)
        exoPlayer.prepare()
    }

    @UnstableApi
    fun setupPlayer(context: Context) {
        player = ExoPlayer.Builder(context)
            .setRenderersFactory(
                DefaultRenderersFactory(context)
                .setEnableDecoderFallback(true)
            )
            .build()
            .also { exoPlayer ->

            preparePlayerWithMediaSource(exoPlayer)

            exoPlayer.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                    if (isPlaying) {
                        startPeriodicProgressUpdate()
                    } else {
                        progressUpdateJob?.cancel()
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == Player.STATE_READY || playbackState == Player.STATE_BUFFERING) {
                        _duration.value = exoPlayer.duration
                    }
                }

                override fun onPositionDiscontinuity(oldPosition: Player.PositionInfo, newPosition: Player.PositionInfo, reason: Int) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    _currentPosition.value = newPosition.positionMs
                }

                override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                    super.onTimelineChanged(timeline, reason)
                    if (!timeline.isEmpty) {
                        _duration.value = exoPlayer.duration
                    }
                }
            })
        }
    }

    private fun startPeriodicProgressUpdate() {
        progressUpdateJob?.cancel()
        progressUpdateJob = viewModelScope.launch {
            while (coroutineContext.isActive) {
                val currentPosition = player.currentPosition
                _currentPosition.value = currentPosition
                delay(1000)
            }
        }
    }

    fun togglePlayPause() {
        if (player.isPlaying) {
            setToPause()
        } else {
            setToPlay()
        }
    }

    private fun setToPause() {
        player.pause()
        _isPlaying.value = false
    }

    private fun setToPlay() {
        player.play()
        _isPlaying.value = true
    }

    fun rewind() {
        val newPosition = (player.currentPosition - 10000).coerceAtLeast(0)
        player.seekTo(newPosition)
    }

    fun fastForward() {
        val newPosition = (player.currentPosition + 10000).coerceAtMost(player.duration)
        player.seekTo(newPosition)
    }

    fun seekTo(position: Long) {
        if (::player.isInitialized && position >= 0 && position <= player.duration) {
            player.seekTo(position)
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
        progressUpdateJob?.cancel()
    }

    fun setCastingState(isCasting: Boolean) {
        _isCasting.value = isCasting
    }
}
