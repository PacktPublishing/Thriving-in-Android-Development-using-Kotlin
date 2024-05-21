package com.packt.stories.ui.filters

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.packt.stories.ui.util.getBitmapFromUri
import kotlin.math.roundToInt

@Composable
fun ImageWithTextOverlay(
    imageUri: Uri,
    modifier: Modifier = Modifier
) {

    var textOverlay = remember { mutableStateOf("") }
    var showTextField = remember { mutableStateOf(false) }
    var textPosition = remember { mutableStateOf(Offset.Zero) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val localContext = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {

        getBitmapFromUri(localContext, imageUri)?.let {
            val imageBitMap = it.asImageBitmap()

            Image(
                bitmap = imageBitMap,
                contentDescription = "Captured Image",
                contentScale = ContentScale.Crop,
                modifier = modifier.matchParentSize()
            )
        }

        if (showTextField.value) {

            TextField(
                value = textOverlay.value,
                onValueChange = { newText: String -> textOverlay.value = newText },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        showTextField.value = false
                    }
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)

            )
        }

        DraggableText(
            text = textOverlay.value,
            position = textPosition.value,
            onPositionChange = { newPosition -> textPosition.value = newPosition },
            modifier = Modifier
                .offset { IntOffset(textPosition.value.x.toInt(), textPosition.value.y.toInt()) }
                .align(Alignment.Center)
        )

        if (!showTextField.value) {
            FloatingActionButton(
                onClick = { showTextField.value = !showTextField.value },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)

            ) {

                Icon(Icons.Default.Edit, contentDescription = "Edit Text")

            }
        }
    }

}

@Composable

fun DraggableText(
    text: String,
    position: Offset,
    onPositionChange: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    var dragOffset = remember { mutableStateOf(position) }

    Text(
        text = text,
        color = Color.White,
        fontSize = 24.sp,
        modifier = modifier
            .offset { IntOffset(dragOffset.value.x.roundToInt(), dragOffset.value.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    dragOffset.value = Offset((dragOffset.value.x + dragAmount.x), (dragOffset.value.y + dragAmount.y))
                    onPositionChange(dragOffset.value)
                }
            }
            .background(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
    )

}