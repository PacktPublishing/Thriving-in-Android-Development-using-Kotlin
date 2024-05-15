package com.packt.stories.ui.editor

import android.graphics.Bitmap
import android.net.Uri
import androidx.camera.core.CameraState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.packt.stories.domain.AddCaptionToVideoUseCase
import com.packt.stories.domain.AddVignetteEffectUseCase
import com.packt.stories.domain.SaveCaptureUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class StoryEditorViewModel(
    private val saveCaptureUseCase: SaveCaptureUseCase,
    private val addCaptionToVideoUseCase: AddCaptionToVideoUseCase,
    private val addVignetteEffectUseCase: AddVignetteEffectUseCase
): ViewModel() {

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing

    private val _imageCaptured: MutableStateFlow<Uri> = MutableStateFlow(Uri.EMPTY)
    val imageCaptured: StateFlow<Uri> = _imageCaptured

    var videoFile: File? = null

    fun storePhotoInGallery(bitmap: Bitmap) {
        viewModelScope.launch {
            val imageUri = saveCaptureUseCase.save(bitmap).getOrNull()
            if (imageUri != null) {
                _imageCaptured.value = imageUri
                _isEditing.value = true
            }
        }
    }

    fun addCaptionToVideo(captionText: String) {
        videoFile?.let { file ->
            viewModelScope.launch {
                val result = addCaptionToVideoUseCase.addCaption(file, captionText)
                // Handle the result of the captioning process
            }
        }
    }

    fun addVignetteFilterToVideo() {
        videoFile?.let { file ->
            viewModelScope.launch {
                val result = addVignetteEffectUseCase.addVignetteEffect(file)
                // Handle the result of the filter process
            }
        }
    }

}
