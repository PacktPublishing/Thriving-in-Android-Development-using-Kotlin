package com.packt.packtagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.packt.newsfeed.ui.NewsFeed
import com.packt.packtagram.ui.MainScreen
import com.packt.packtagram.ui.theme.PacktagramTheme
import com.packt.stories.ui.editor.StoryContent
import com.packt.stories.ui.editor.StoryEditorScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PacktagramTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StoryEditorScreen()
                //Comenting this as in this chapter we will work with the StoryEditor
                //  and we are not implemeting navigation in this app to focus in the features
                //  MainScreen()
                }
            }
        }
    }
}