package dev.dot.files.smb

import java.io.IOException
import kotlin.jvm.Throws

class Connection(private val authority: Authority) {
    var isOpen: Boolean = false
        private set

    @Throws(IOException::class)
    fun connect() {
        if (isOpen) {
            // TODO: Evaluate if this needs to be handled differently
            return
        }
    }
}
