package com.packt.stories.ui.editor

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.AlphabeticIndex
import android.media.Image
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Consumer
import androidx.navigation.compose.rememberNavController
import com.packt.stories.R
import com.packt.stories.ui.rotateBitmap
import com.packt.stories.ui.toBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable

fun StoryContent(

    isEditing: Boolean = false,

    modifier: Modifier = Modifier

) {

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()

        ) {

            Button(onClick = { /*Handle back*/ }, modifier = Modifier.align(Alignment.TopStart)) {

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



        Image(
            painter = painterResource(id = R.drawable.ic_default_image),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "Default image"
        )



        Row(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
        ) {

            if (isEditing) {

                Button(onClick = { /* Handle create caption */ }) {

                    Text(stringResource(id = R.string.share_story))

                }

            } else {

                OutlinedButton(
                    onClick = { /* Handle take a photo */ },
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)

                ) {

                }

            }

        }

    }

}
