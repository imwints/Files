package dev.dot.files.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.IOException
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

// TODO: Allow creation of parent directories
class NewDirectoryWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val path = Path(inputData.getString(KEY_DIRECTORY_PATH)!!)
                require(path.isAbsolute) { "Cannot create directory from relative path" }
                path.createDirectory()
                Timber.d("Created directory at $path")
                Result.success()
            } catch (e: IOException) {
                Timber.e(e, "Failed to create directory")
                Result.failure()
            }
        }
    }

    companion object {
        const val KEY_DIRECTORY_PATH = "KEY_DIRECTORY_PATH"
    }
}
