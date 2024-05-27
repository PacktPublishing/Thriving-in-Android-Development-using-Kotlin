package com.packt.packtflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.packt.login.presentation.LoginScreen
import com.packt.packtflix.ui.theme.PacktflixTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PacktflixTheme {
                // For simplification, this app doesn't have navigation implemented
                // but you can check here the different screens, uncommenting the one you want
                // to test

                LoginScreen()
                //MoviesScreenUI()
                //ItemDetailScreen()
            }
        }
    }
}
