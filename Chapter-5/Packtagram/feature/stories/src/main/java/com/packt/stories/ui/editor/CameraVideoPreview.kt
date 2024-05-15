package com.packt.stories.ui.editor

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun CameraVideoPreview(
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    modifier: Modifier = Modifier,
): VideoCapture<Recorder> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderState = remember { mutableStateOf<ProcessCameraProvider?>(null) }

    // Create a Preview instance
    val preview = Preview.Builder().build()

    // Initialize Recorder with a quality selector
    val recorder = Recorder.Builder()
        .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
        .build()

    // Use Recorder to build VideoCapture
    val videoCapture = VideoCapture.withOutput(recorder)

    // Set up the DisposableEffect for CameraX
    DisposableEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()
        cameraProviderState.value = cameraProvider

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


        try {
            // Unbind all use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                videoCapture
            )
        } catch (exc: Exception) {
            // Handle exceptions
        }

        onDispose {
            cameraProviderState.value?.unbindAll()
        }
    }

    // AndroidView for displaying the camera preview
    AndroidView(
        factory = { PreviewView(it) },
        update = { view ->
            preview.setSurfaceProvider(view.surfaceProvider)
        },
        modifier = modifier
    )

    return videoCapture
}



/**
@Composable
private fun CaptureButton(
localContext: Context,
cameraController: LifecycleCameraController,
onImageCaptured: (Bitmap) -> Any,
videoCapture: VideoCapture<Recorder>?,
recording: Recording?,
onVideoCaptured: (String) -> Any
) {
var videoRecording = recording

/**
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
if (videoRecording == null) {
videoRecording =
startRecording(it, localContext, onVideoCaptured)
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
videoRecording = null
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
}8**/
} **/