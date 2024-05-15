package com.packt.stories.ui.editor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.packt.stories.R
import com.packt.stories.ui.filters.BlackAndWhiteFilter
import com.packt.stories.ui.filters.ImageFilter
import com.packt.stories.ui.rotateBitmap
import com.packt.stories.ui.toBitmap
import java.io.File
import java.util.concurrent.Executors

@Composable
fun StoryContent(
    isEditing: Boolean = false,
    imageCaptured: Uri? = null,
    onImageCaptured: (Bitmap) -> Any,
    onVideoCaptured: (String) -> Any,
    modifier: Modifier = Modifier,
) {
    val localContext = LocalContext.current
    val cameraController = remember { LifecycleCameraController(localContext) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val filterApplied = remember { mutableStateOf(ImageFilter.BLACK_AND_WHITE) }

    DisposableEffect(lifecycleOwner) {
        cameraController.bindToLifecycle(lifecycleOwner)
        onDispose {
            cameraController.unbind()
        }
    }

    if (isEditing) {
        EditionModeContent(
            modifier = modifier,
            imageCaptured = imageCaptured,
            filterApplied = filterApplied,
        )
    } else {
        CaptureModeContent(
            cameraController = cameraController,
            onImageCaptured = onImageCaptured,
            onVideoCaptured = onVideoCaptured
        )
    }
}

@Composable
private fun CaptureModeContent(
    cameraController: LifecycleCameraController,
    onImageCaptured: (Bitmap) -> Any,
    onVideoCaptured: (String) -> Any
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CameraPermissionRequester {
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
                CameraPreview(
                    cameraController = cameraController,
                    modifier = Modifier.fillMaxSize()
                )
                Row {
                    CaptureButton(
                        cameraController = cameraController,
                        onPhotoCaptured = { bitmap ->
                            onImageCaptured(bitmap)
                        },
                        onError = { exception ->
                            // Handle any errors
                        }
                    )
                    CaptureVideoButton(
                        cameraController = cameraController,
                        onRecordingFinished = { videoPath ->
                            onVideoCaptured(videoPath)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EditionModeContent(
    modifier: Modifier,
    imageCaptured: Uri?,
    filterApplied: MutableState<ImageFilter>,
) {
    Box(modifier = modifier.fillMaxSize()) {
        imageCaptured?.let {
            when (filterApplied.value) {
                ImageFilter.BLACK_AND_WHITE -> {
                    BlackAndWhiteFilter(imageUri = imageCaptured, modifier = modifier.fillMaxSize())
                }

                ImageFilter.OTHER -> {
                    // Apply vignette filter
                }

                else -> {
                    imageCaptured?.let {
                        LocalImageDisplay(
                            it, modifier = modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                        )
                    }
                    EditionControls()
                }

            }
        }
    }
}

@Composable
fun CameraPreview(cameraController: LifecycleCameraController, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            PreviewView(context).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = modifier,
        update = { previewView ->
            previewView.controller = cameraController
        }
    )
}

@Composable
fun CaptureButton(
    cameraController: LifecycleCameraController,
    onPhotoCaptured: (Bitmap) -> Unit,
    onError: (Exception) -> Unit
) {
    val context = LocalContext.current
    IconButton(
        onClick = {
            cameraController.setEnabledUseCases(LifecycleCameraController.IMAGE_CAPTURE)
            capturePhoto(
                context = context,
                cameraController = cameraController,
                onPhotoCaptured = onPhotoCaptured,
                onError = onError
            )
        },
        modifier = Modifier
            .size(60.dp)
            .padding(8.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = "Capture video",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun CaptureVideoButton(
    cameraController: LifecycleCameraController,
    onRecordingFinished: (String) -> Unit,
) {

    val context = LocalContext.current
    val recording = remember { mutableStateOf<Recording?>(null) }

    IconButton(
        onClick = {
            cameraController.setEnabledUseCases(LifecycleCameraController.VIDEO_CAPTURE)
            if (recording.value == null) {
                recording.value = startRecording(cameraController, context, onRecordingFinished)
            } else {
                stopRecording(recording.value)
                recording.value = null
            }
        },
        modifier = Modifier
            .size(60.dp)
            .padding(8.dp),
    ) {
        Icon(
            painter = if (recording.value == null) painterResource(id = R.drawable.ic_videocam) else painterResource(id = R.drawable.ic_stop),
            contentDescription = "Capture video",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

fun capturePhoto(
    cameraController: LifecycleCameraController,
    context: Context,
    onPhotoCaptured: (Bitmap) -> Unit,
    onError: (Exception) -> Unit
) {

    cameraController.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            @androidx.annotation.OptIn(ExperimentalGetImage::class)
            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                val image = imageProxy.image
                image?.let {
                    val bitmap = image.toBitmap().rotateBitmap(imageProxy.imageInfo.rotationDegrees)
                    onPhotoCaptured(bitmap)
                }
                imageProxy.close()
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraContent", "Error capturing image: ${exception.message}", exception)
                onError(exception)
            }
        }
    )
}

@Composable
fun LocalImageDisplay(imageUri: Uri, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUri)
            .build(),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun EditionControls() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Button(
            onClick = { /*Handle back*/ },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(6.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back button"
            )
        }
        Button(
            onClick = { /* Handle create caption */ },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_caption),
                contentDescription = "Create label"
            )
        }
        Button(
            onClick = { /* Handle filter */ },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Messages")
        }

    }
}

@SuppressLint("MissingPermission")
private fun startRecording(
    cameraController: LifecycleCameraController,
    context: Context,
    onRecordingFinished: (String) -> Unit
): Recording {
    val videoFile = File(context.filesDir, "video_${System.currentTimeMillis()}.mp4")
    val outputOptions = FileOutputOptions.Builder(videoFile).build()
    val audioConfig = AudioConfig.create(true)
    val executor = Executors.newSingleThreadExecutor()

    return cameraController.startRecording(
        outputOptions,
        audioConfig,
        executor
    ) { recordEvent ->
        when (recordEvent) {
            is VideoRecordEvent.Finalize -> {
                if (recordEvent.hasError()) {
                    Log.e("CaptureVideoButton", "Video recording error: ${recordEvent.error}")
                } else {
                    onRecordingFinished(videoFile.absolutePath)
                }
            }
        }
    }
}

fun stopRecording(recording: Recording?) {
    recording?.stop()
}
