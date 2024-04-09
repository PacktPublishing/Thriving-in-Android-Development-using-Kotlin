package com.packt.packtflix

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.packt.list.presentation.detail.ItemDetailScreen
import com.packt.list.presentation.detail.model.ItemDetail
import com.packt.list.presentation.list.MoviesScreenUI
import com.packt.login.presentation.LoginScreen
import com.packt.packtflix.ui.theme.PacktflixTheme
import com.packt.playback.presentation.PlaybackActivity
import com.packt.playback.presentation.PlaybackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PacktflixTheme {
                //LoginScreen()
                //MoviesScreenUI()
                //ItemDetailScreen()
                //PlaybackScreen()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val intent = Intent(this, PlaybackActivity::class.java)
        startActivity(intent)
    }
}
