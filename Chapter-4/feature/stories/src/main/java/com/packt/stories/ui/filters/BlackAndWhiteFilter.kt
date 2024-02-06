package com.packt.stories.ui.filters

import androidx.compose.runtime.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import android.graphics.Bitmap
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix

@Composable
fun BlackAndWhiteFilter(bitmap: Bitmap) {
    var isBlackAndWhiteEnabled by remember { mutableStateOf(false) }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawImage(bitmap.asImageBitmap())

        if (isBlackAndWhiteEnabled) {
            val colorMatrix = ColorMatrix().apply { setToSaturation(0f) }
            val colorFilter = ColorFilter.colorMatrix(colorMatrix)

            drawImage(
                image = bitmap.asImageBitmap(),
                colorFilter = colorFilter
            )
        }
    }

    OutlinedButton(
        onClick = { isBlackAndWhiteEnabled = !isBlackAndWhiteEnabled },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Apply Black and White Filter")
    }
}
