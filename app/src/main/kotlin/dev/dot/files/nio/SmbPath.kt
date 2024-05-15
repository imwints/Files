package dev.dot.files.nio

import java.io.File
import java.net.URI
import java.nio.file.FileSystem
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService

class SmbPath(private val fileSystem: SmbFileSystem, private val path: String) : Path {
    override fun compareTo(other: Path?): Int {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<Path> {
        TODO("Not yet implemented")
    }

    override fun register(
        watcher: WatchService?,
        events: Array<out WatchEvent.Kind<*>>?,
        vararg modifiers: WatchEvent.Modifier?,
    ): WatchKey {
        TODO("Not yet implemented")
    }

    override fun register(watcher: WatchService?, vararg events: WatchEvent.Kind<*>?): WatchKey {
        TODO("Not yet implemented")
    }

    override fun getFileSystem(): FileSystem {
        TODO("Not yet implemented")
    }

    override fun isAbsolute(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getRoot(): Path {
        TODO("Not yet implemented")
    }

    override fun getFileName(): Path {
        TODO("Not yet implemented")
    }

    override fun getParent(): Path {
        TODO("Not yet implemented")
    }

    override fun getNameCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getName(index: Int): Path {
        TODO("Not yet implemented")
    }

    override fun subpath(beginIndex: Int, endIndex: Int): Path {
        TODO("Not yet implemented")
    }

    override fun startsWith(other: Path?): Boolean {
        TODO("Not yet implemented")
    }

    override fun startsWith(other: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun endsWith(other: Path?): Boolean {
        TODO("Not yet implemented")
    }

    override fun endsWith(other: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun normalize(): Path {
        TODO("Not yet implemented")
    }

    override fun resolve(other: Path?): Path {
        TODO("Not yet implemented")
    }

    override fun resolve(other: String?): Path {
        TODO("Not yet implemented")
    }

    override fun resolveSibling(other: Path?): Path {
        TODO("Not yet implemented")
    }

    override fun resolveSibling(other: String?): Path {
        TODO("Not yet implemented")
    }

    override fun relativize(other: Path?): Path {
        TODO("Not yet implemented")
    }

    override fun toUri(): URI {
        TODO("Not yet implemented")
    }

    override fun toAbsolutePath(): Path {
        TODO("Not yet implemented")
    }

    override fun toRealPath(vararg options: LinkOption?): Path {
        TODO("Not yet implemented")
    }

    override fun toFile(): File {
        TODO("Not yet implemented")
    }
}
