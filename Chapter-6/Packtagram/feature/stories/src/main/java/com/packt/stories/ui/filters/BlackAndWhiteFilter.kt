package com.packt.stories.ui.filters

import androidx.compose.runtime.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import android.net.Uri
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import com.packt.stories.ui.util.getBitmapFromUri

@Composable
fun BlackAndWhiteFilter(
    imageUri: Uri,
    modifier: Modifier = Modifier
) {
    var isBlackAndWhiteEnabled by remember { mutableStateOf(false) }
    val localContext = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            getBitmapFromUri(localContext, imageUri)?.let {
                val imageBitMap = it.asImageBitmap()

                val colorFilter = if (isBlackAndWhiteEnabled) {
                    val colorMatrix = ColorMatrix().apply { setToSaturation(0f) }
                    ColorFilter.colorMatrix(colorMatrix)
                } else {
                    null
                }
                val (offsetX, offsetY) = getCanvasImageOffset(imageBitMap)
                val scaleFactor = getCanvasImageScale(imageBitMap)

                with(drawContext.canvas) {
                    save()
                    translate(offsetX, offsetY)
                    scale(scaleFactor, scaleFactor)
                    drawImage(
                        image = imageBitMap,
                        topLeft = androidx.compose.ui.geometry.Offset.Zero,
                        colorFilter = colorFilter
                    )
                    restore()
                }
            }
        }

        Button(
            onClick = { isBlackAndWhiteEnabled = !isBlackAndWhiteEnabled },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Apply Black and White Filter")
        }
    }
}

private fun DrawScope.getCanvasImageOffset(image: ImageBitmap): Pair<Float, Float> {
    val canvasWidth = size.width
    val canvasHeight = size.height
    val imageWidth = image.width.toFloat()
    val imageHeight = image.height.toFloat()
    val scaleX = canvasWidth / imageWidth
    val scaleY = canvasHeight / imageHeight
    val scale = maxOf(scaleX, scaleY)

    // Calculate the offset to center the image
    val offsetX = (canvasWidth - (imageWidth * scale)) / 2
    val offsetY = (canvasHeight - (imageHeight * scale)) / 2

    return offsetX to offsetY
}

private fun DrawScope.getCanvasImageScale(image: ImageBitmap): Float {
    val canvasWidth = size.width
    val canvasHeight = size.height
    val imageWidth = image.width.toFloat()
    val imageHeight = image.height.toFloat()
    val scaleX = canvasWidth / imageWidth
    val scaleY = canvasHeight / imageHeight
    return maxOf(scaleX, scaleY)
}
