package com.packt.pictureInPicture.util

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun CheckPipCompatibility(onPipAvailable: @Composable () -> Unit) {
    var isPipCompatible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val minApiLevel = Build.VERSION_CODES.O // Minimum API level for PiP is Oreo (8.0)
        isPipCompatible = Build.VERSION.SDK_INT >= minApiLevel
    }

    if (isPipCompatible) {
        onPipAvailable()
    } else {
        // Show a message or alternative functionality if PiP is not compatible
    }
}
