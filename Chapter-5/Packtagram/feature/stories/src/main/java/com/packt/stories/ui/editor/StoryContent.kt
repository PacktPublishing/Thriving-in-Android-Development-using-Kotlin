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
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
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
    var videoCapture: VideoCapture<Recorder>? by remember { mutableStateOf(null) }
    var recording: Recording? by remember { mutableStateOf(null) }
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
        CaptureModeContent(cameraController, onImageCaptured)
    }
}

@Composable
private fun CaptureModeContent(
    cameraController: LifecycleCameraController,
    onImageCaptured: (Bitmap) -> Any
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CameraPermissionRequester {
            Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
                CameraPreviewWithImageLabeler(
                    cameraController = cameraController,
                    modifier = Modifier.fillMaxSize()
                )
                CaptureButton(
                    cameraController = cameraController,
                    onPhotoCaptured = { bitmap ->
                        onImageCaptured(bitmap)
                    },
                    onError = { exception ->
                        // Handle any errors
                    }
                )
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

                ImageFilter.VIGNETTE -> {
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
    Button(
        onClick = {
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
        shape = CircleShape
    ) {
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
fun startRecording(
    videoCapture: VideoCapture<Recorder>,
    context: Context,
    onVideoCaptured: (String) -> Any
): Recording {
    val videoFile = File(context.getExternalFilesDir(null), "video.mp4")
    val outputFileOptions = FileOutputOptions.Builder(videoFile).build()

    val listenerExecutor = Executors.newSingleThreadExecutor() // Executor for handling events

    val listener = Consumer<VideoRecordEvent> { event ->
        when (event) {
            is VideoRecordEvent.Finalize -> {
                if (event.hasError()) {
                    // Check if there was an error finalizing the recording
                }
                onVideoCaptured(videoFile.absolutePath)
            }
        }
    }

    return videoCapture.output.prepareRecording(context, outputFileOptions)
        .withAudioEnabled()
        .start(listenerExecutor, listener)
}

fun stopRecording(recording: Recording?) {
    recording?.stop()
}
