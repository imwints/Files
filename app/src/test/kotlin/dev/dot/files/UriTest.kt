package dev.dot.files

import java.net.URI
import org.junit.Test

class UriTest {
    @Test
    fun `test SmbScheme`() {
        val uri = URI.create("smb://steffen@192.168.33.5:200/src")
        print(uri.scheme)
        print(uri.userInfo)
        print(uri.host)
        print(uri.port)
        println(uri.path)
        println(uri.authority)
    }
}
