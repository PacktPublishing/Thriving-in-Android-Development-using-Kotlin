package com.packt.stories.domain

import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.FFmpeg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AddVignetteEffectUseCase() {

    suspend fun addVignetteEffect(videoFile: File): Result<File> = withContext(Dispatchers.IO) {
        val outputFile = File(videoFile.parent, "${videoFile.nameWithoutExtension}_vignetted.mp4")
        val command = "-i ${videoFile.absolutePath} -vf vignette=angle=PI/4 ${outputFile.absolutePath}"

        try {
            val executionId = FFmpeg.executeAsync(command) { _, returnCode ->
                if (returnCode != Config.RETURN_CODE_SUCCESS) {
                    Result.failure<AddVignetteEffectError>(AddVignetteEffectError)
                }
            }
            // Optionally handle the executionId, e.g., for cancellation
            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

object AddVignetteEffectError : Throwable("There was an error adding the vignette effect to the video") {
    private fun readResolve(): Any = AddVignetteEffectError
}
