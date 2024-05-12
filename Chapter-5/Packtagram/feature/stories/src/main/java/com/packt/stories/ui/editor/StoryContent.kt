package com.packt.stories.ui.editor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import com.packt.stories.R
import com.packt.stories.ui.rotateBitmap
import com.packt.stories.ui.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StoryContent(
    isEditing: Boolean = false,
    onImageCaptured: (Bitmap) -> Any,
    onVideoCaptured: (String) -> Any,
    modifier: Modifier = Modifier,
) {
    val localContext = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(localContext) }
    var videoCapture: VideoCapture<Recorder>? by remember { mutableStateOf(null) }
    var recording: Recording? by remember { mutableStateOf(null) }
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(localContext) }

    fun capturePhoto(
        context: Context,
        cameraController: LifecycleCameraController,
        onPhotoCaptured: (Bitmap) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

        cameraController.takePicture(
            mainExecutor,
            @ExperimentalGetImage object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    try {
                        CoroutineScope(Dispatchers.IO).launch {
                            val correctedBitmap: Bitmap? = image
                                ?.image
                                ?.toBitmap()
                                ?.rotateBitmap(image.imageInfo.rotationDegrees)

                            correctedBitmap?.let {
                                withContext(Dispatchers.Main) {
                                    onPhotoCaptured(correctedBitmap)
                                }
                            }

                            image.close()
                        }
                    } catch (e: Exception) {
                        onError(e)
                    } finally {
                        image.close()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraContent", "Error capturing image", exception)
                    onError(exception)
                }
            })
    }


    Box(modifier = Modifier.fillMaxSize()) {
        CameraPermissionRequester {
            videoCapture =
                cameraVideoPreview(cameraProviderFuture, modifier = Modifier.fillMaxSize())
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Button(onClick = { /*Handle back*/ }, modifier = Modifier.align(Alignment.TopStart).padding(6.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back button"
                )
            }
            if (isEditing) {
                Button(
                    onClick = { /* Handle create caption */ },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_caption),
                        contentDescription = "Create label"
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(10.dp)
                .align(Alignment.BottomCenter)
        ) {
            if (isEditing) {
                Button(onClick = { /* Handle create caption */ }) {
                    Text(stringResource(id = R.string.share_story))
                }
            } else {
                Button(
                    onClick = {
                        Log.d("VideoCapture", "on click")

                        capturePhoto(localContext, cameraController, { bitmap ->
                            onImageCaptured(bitmap)
                        }, { exception ->
                            // Handle error
                        })
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .pointerInput("videoCaptureButton") {
                            detectTapGestures(
                                onLongPress = {
                                    Log.d("VideoCapture", "Long press detected")
                                    videoCapture?.let {
                                        if (recording == null) {
                                            recording = startRecording(it, localContext, onVideoCaptured)
                                        }
                                    }
                                },
                                onPress = {
                                    Log.d("VideoCapture", "Single press detected")
                                    try {
                                        awaitRelease()
                                    } finally {
                                        if (recording != null) {
                                            stopRecording(recording)
                                            recording = null
                                        } else {
                                            // Capture photo
                                            capturePhoto(localContext, cameraController, { bitmap ->
                                                onImageCaptured(bitmap)
                                            }, { exception ->
                                                // Handle error
                                            })
                                        }
                                    }
                                }
                            )
                        },
                    shape = CircleShape,
                    border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(0.dp),
                ) {
                }
            }

        }
    }
}

@SuppressLint("MissingPermission")
fun startRecording(videoCapture: VideoCapture<Recorder>, context: Context, onVideoCaptured: (String) -> Any): Recording {
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
