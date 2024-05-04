package dev.dot.files.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.IOException
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.deleteRecursively
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class DeleteWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    @OptIn(ExperimentalPathApi::class)
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val path = Path(inputData.getString(KEY_TO_DELETE_PATH)!!)
                require(path.isAbsolute) { "Cannot delete file from relative path" }
                path.deleteRecursively()
                Result.success()
            } catch (e: IOException) {
                Timber.e(e, "Failed to delete file")
                Result.failure()
            }
        }
    }

    companion object {
        const val KEY_TO_DELETE_PATH = "KEY_TO_DELETE_PATH"
    }
}
