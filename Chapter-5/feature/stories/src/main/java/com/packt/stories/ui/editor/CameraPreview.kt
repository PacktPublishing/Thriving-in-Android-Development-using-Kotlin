package com.packt.stories.ui.editor

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun CameraPreview(
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderState = remember { mutableStateOf<ProcessCameraProvider?>(null) }

    // Directly create a Preview instance without wrapping in a state
    val preview = Preview.Builder().build()

    DisposableEffect(cameraProviderFuture) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProviderState.value = cameraProvider
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
        }, ContextCompat.getMainExecutor(context))

        onDispose { }
    }

    AndroidView(
        factory = { PreviewView(it) },
        update = { view ->
            // Use the preview directly without .value
            preview.setSurfaceProvider(view.surfaceProvider)
        },
        modifier = modifier
    )
}



