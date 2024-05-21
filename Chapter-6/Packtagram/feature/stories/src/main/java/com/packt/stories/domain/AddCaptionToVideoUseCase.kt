package com.packt.stories.domain

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AddCaptionToVideoUseCase() {

    suspend fun addCaption(videoFile: File, captionText: String): Result<File> = withContext(Dispatchers.IO) {
        val outputFile = File(videoFile.parent, "${videoFile.nameWithoutExtension}_captioned.mp4")
        val fontFilePath = "/system/fonts/Roboto-Regular.ttf"
        val ffmpegCommand = arrayOf(
            "-i", videoFile.absolutePath,
            "-vf", "drawtext=fontfile=$fontFilePath:text='$captionText':fontcolor=white:fontsize=24:x=(w-text_w)/2:y=(h-text_h)-10",
            "-c:a", "aac",
            "-b:a", "192k",
            outputFile.absolutePath
        )

        try {
            val executionId = FFmpeg.executeAsync(ffmpegCommand) { _, returnCode ->
                if (returnCode != Config.RETURN_CODE_SUCCESS) {
                    Result.failure<AddCaptionToVideoError>(AddCaptionToVideoError)
                }
            }
            // Optionally handle the executionId, e.g., for cancellation
            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

object AddCaptionToVideoError: Throwable("There was an error adding the caption to the video") {
    private fun readResolve(): Any = AddCaptionToVideoError
}
