package dev.dot.files.nio

import java.net.URI
import java.nio.channels.SeekableByteChannel
import java.nio.file.AccessMode
import java.nio.file.CopyOption
import java.nio.file.DirectoryStream
import java.nio.file.FileStore
import java.nio.file.FileSystem
import java.nio.file.FileSystemAlreadyExistsException
import java.nio.file.FileSystemNotFoundException
import java.nio.file.LinkOption
import java.nio.file.OpenOption
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileAttribute
import java.nio.file.attribute.FileAttributeView
import java.nio.file.spi.FileSystemProvider
import java.util.concurrent.ConcurrentHashMap

class SmbFileSystemProvider : FileSystemProvider() {
    private val cache = ConcurrentHashMap<String, SmbFileSystem>()

    override fun getScheme() = SCHEME

    private fun checkUri(uri: URI) {
        require(uri.scheme.lowercase() == scheme)
        require(uri.host.isNotEmpty())
    }

    private fun createFileSystem(authority: String) = SmbFileSystem(this, authority)

    override fun newFileSystem(uri: URI?, env: MutableMap<String, *>?): FileSystem {
        checkUri(uri!!)
        val authority = uri.authority
        if (cache.containsKey(authority)) {
            throw FileSystemAlreadyExistsException(authority)
        }
        val fileSystem = createFileSystem(authority)
        cache[authority] = fileSystem
        return fileSystem
    }

    override fun getFileSystem(uri: URI?): FileSystem {
        checkUri(uri!!)
        val authority = uri.authority
        return cache[authority] ?: throw FileSystemNotFoundException(authority)
    }

    private fun ensureFileSystem(uri: URI): FileSystem {
        val authority = uri.authority
        return cache[authority] ?: createFileSystem(authority)
    }

    override fun getPath(uri: URI?): Path {
        checkUri(uri!!)
        return ensureFileSystem(uri).getPath(uri.path)
    }

    override fun newByteChannel(
        path: Path?,
        options: MutableSet<out OpenOption>?,
        vararg attrs: FileAttribute<*>?,
    ): SeekableByteChannel {
        TODO("Not yet implemented")
    }

    override fun newDirectoryStream(
        dir: Path?,
        filter: DirectoryStream.Filter<in Path>?,
    ): DirectoryStream<Path> {
        TODO("Not yet implemented")
    }

    override fun createDirectory(dir: Path?, vararg attrs: FileAttribute<*>?) {
        TODO("Not yet implemented")
    }

    override fun delete(path: Path?) {
        TODO("Not yet implemented")
    }

    override fun copy(source: Path?, target: Path?, vararg options: CopyOption?) {
        TODO("Not yet implemented")
    }

    override fun move(source: Path?, target: Path?, vararg options: CopyOption?) {
        TODO("Not yet implemented")
    }

    override fun isSameFile(path: Path?, path2: Path?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isHidden(path: Path?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getFileStore(path: Path?): FileStore {
        TODO("Not yet implemented")
    }

    override fun checkAccess(path: Path?, vararg modes: AccessMode?) {
        TODO("Not yet implemented")
    }

    override fun <V : FileAttributeView?> getFileAttributeView(
        path: Path?,
        type: Class<V>?,
        vararg options: LinkOption?,
    ): V {
        TODO("Not yet implemented")
    }

    override fun <A : BasicFileAttributes?> readAttributes(
        path: Path?,
        type: Class<A>?,
        vararg options: LinkOption?,
    ): A {
        TODO("Not yet implemented")
    }

    override fun readAttributes(
        path: Path?,
        attributes: String?,
        vararg options: LinkOption?,
    ): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }

    override fun setAttribute(
        path: Path?,
        attribute: String?,
        value: Any?,
        vararg options: LinkOption?,
    ) {
        TODO("Not yet implemented")
    }

    companion object {
        const val SCHEME = "smb"
    }
}
