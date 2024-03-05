package com.packt.packtflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.packt.list.presentation.list.MoviesScreenUI
import com.packt.login.presentation.LoginScreen
import com.packt.packtflix.ui.theme.PacktflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PacktflixTheme {
                // LoginScreen()
                MoviesScreenUI()
            }
        }
    }
}
