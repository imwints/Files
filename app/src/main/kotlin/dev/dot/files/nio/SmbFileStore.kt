package dev.dot.files.nio

import java.nio.file.FileStore
import java.nio.file.attribute.FileAttributeView
import java.nio.file.attribute.FileStoreAttributeView

class SmbFileStore : FileStore() {
    override fun name(): String {
        TODO("Not yet implemented")
    }

    override fun type(): String {
        TODO("Not yet implemented")
    }

    override fun isReadOnly(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTotalSpace(): Long {
        TODO("Not yet implemented")
    }

    override fun getUsableSpace(): Long {
        TODO("Not yet implemented")
    }

    override fun getUnallocatedSpace(): Long {
        TODO("Not yet implemented")
    }

    override fun supportsFileAttributeView(type: Class<out FileAttributeView>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun supportsFileAttributeView(name: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun <V : FileStoreAttributeView?> getFileStoreAttributeView(type: Class<V>?): V {
        TODO("Not yet implemented")
    }

    override fun getAttribute(attribute: String?): Any {
        TODO("Not yet implemented")
    }
}