package dev.dot.files.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.IOException
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

// TODO: Allow creation of parent directories
class NewFileWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val path = Path(inputData.getString(KEY_FILE_PATH)!!)
                require(path.isAbsolute) { "Cannot create file from relative path" }
                path.createFile()
                Timber.d("Created file at $path")
                Result.success()
            } catch (e: IOException) {
                Timber.e(e, "Failed to create file")
                Result.failure()
            }
        }
    }

    companion object {
        const val KEY_FILE_PATH = "KEY_FILE_PATH"
    }
}
