package dev.dot.files.data.filesystem

import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.deleteRecursively
import kotlin.io.path.listDirectoryEntries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FileSystemRepository {
    suspend fun createDirectory(path: Path) = withContext(Dispatchers.IO) { path.createDirectory() }

    suspend fun createFile(path: Path) = withContext(Dispatchers.IO) { path.createFile() }

    @OptIn(ExperimentalPathApi::class)
    suspend fun delete(path: Path) = withContext(Dispatchers.IO) { path.deleteRecursively() }

    suspend fun listFiles(path: Path) =
        withContext(Dispatchers.IO) {
            return@withContext path.listDirectoryEntries()
        }
}
