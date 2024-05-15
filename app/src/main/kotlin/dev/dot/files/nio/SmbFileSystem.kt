package dev.dot.files.nio

import java.nio.file.FileStore
import java.nio.file.FileSystem
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.PathMatcher
import java.nio.file.WatchService
import java.nio.file.attribute.UserPrincipalLookupService

class SmbFileSystem(private val provider: SmbFileSystemProvider, private val authority: String) :
    FileSystem() {
    private val rootDirectory = SmbPath(this, "/")

    init {}

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun provider() = provider

    override fun isOpen() = true

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
        FileSystems.getDefault().newWatchService()
        TODO("Not yet implemented")
    }

    companion object {
        const val SEPARATOR = "/"
    }
}
