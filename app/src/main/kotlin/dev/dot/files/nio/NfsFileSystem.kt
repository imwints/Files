package dev.dot.files.nio

import java.nio.file.FileStore
import java.nio.file.FileSystem
import java.nio.file.Path
import java.nio.file.PathMatcher
import java.nio.file.WatchService
import java.nio.file.attribute.UserPrincipalLookupService
import java.nio.file.spi.FileSystemProvider

class NfsFileSystem(
    private val provider: NfsFileSystemProvider,
    private val host: String,
) : FileSystem() {
    private var isOpen = false

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun provider(): FileSystemProvider {
        TODO("Not yet implemented")
    }

    override fun isOpen() = isOpen

    override fun isReadOnly() = false

    override fun getSeparator() = SEPARATOR

    override fun getRootDirectories(): MutableIterable<Path> {
        TODO("Not yet implemented")
    }

    override fun getFileStores(): MutableIterable<FileStore> {
        TODO("Not yet implemented")
    }

    override fun supportedFileAttributeViews(): MutableSet<String> {
        TODO("Not yet implemented")
    }

    override fun getPath(first: String?, vararg more: String?): Path {
        TODO("Not yet implemented")
    }

    override fun getPathMatcher(syntaxAndPattern: String?): PathMatcher {
        TODO("Not yet implemented")
    }

    override fun getUserPrincipalLookupService(): UserPrincipalLookupService {
        TODO("Not yet implemented")
    }

    override fun newWatchService(): WatchService {
        TODO("Not yet implemented")
    }

    companion object {
        const val SEPARATOR = "/"
    }
}
