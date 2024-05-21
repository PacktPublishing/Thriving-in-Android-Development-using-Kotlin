package com.packt.stories.domain

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveCaptureUseCase(private val context: Context) {

    companion object {
        private const val IMAGE_QUALITY = 100
        private const val FILE_NAME_PREFIX = "YourImageName"
        private const val AUTHOR_NAME = "Your Name"
        private const val DESCRIPTION = "Your description"
    }

    suspend fun save(capturePhotoBitmap: Bitmap): Result<Uri> = withContext(Dispatchers.IO) {
        val resolver: ContentResolver = context.applicationContext.contentResolver
        val imageCollection = getImageCollectionUri()
        val nowTimestamp = System.currentTimeMillis()
        val imageContentValues = createContentValues(nowTimestamp)

        val imageMediaStoreUri: Uri? = resolver.insert(imageCollection, imageContentValues)

        return@withContext imageMediaStoreUri?.let { uri ->
            saveBitmapToUri(resolver, uri, capturePhotoBitmap, imageContentValues)
        } ?: Result.failure(Exception("Couldn't create file for gallery"))
    }

    private fun getImageCollectionUri(): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

    private fun createContentValues(timestamp: Long): ContentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$FILE_NAME_PREFIX${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.DATE_TAKEN, timestamp)
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DCIM}/YourAppNameOrSubFolderName")
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            put(MediaStore.Images.Media.DATE_ADDED, timestamp)
            put(MediaStore.Images.Media.DATE_MODIFIED, timestamp)
            put(MediaStore.Images.Media.AUTHOR, AUTHOR_NAME)
            put(MediaStore.Images.Media.DESCRIPTION, DESCRIPTION)
        }
    }

    private fun saveBitmapToUri(
        resolver: ContentResolver,
        uri: Uri,
        bitmap: Bitmap,
        contentValues: ContentValues
    ): Result<Uri> = kotlin.runCatching {
        resolver.openOutputStream(uri).use { outputStream ->
            checkNotNull(outputStream) { "Couldn't create file for gallery, MediaStore output stream is null" }
            bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }

        return Result.success(uri)
    }.getOrElse { exception ->
        exception.message?.let(::println)
        resolver.delete(uri, null, null)
        return Result.failure(exception)
    }
}
