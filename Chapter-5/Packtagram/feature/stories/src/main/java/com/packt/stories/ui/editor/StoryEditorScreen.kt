package com.packt.stories.ui.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryEditorScreen(
    viewModel: StoryEditorViewModel = koinViewModel()
) {
    val isEditing = viewModel.isEditing.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        StoryContent(
            isEditing = isEditing.value,
            imageCaptured = viewModel.imageCaptured.collectAsState().value,
            onImageCaptured = { viewModel.storePhotoInGallery(it) },
            onVideoCaptured = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
