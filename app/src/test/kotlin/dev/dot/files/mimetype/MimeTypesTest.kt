package dev.dot.files.mimetype

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MimeTypesTest {
    @Test
    fun `test findMimeTypeByExtension`() {
        // Given
        val extension = "jpeg"

        // When
        val actual = MimeTypes.getMimeType(extension)

        // Then
        assertThat(actual.toString()).isEqualTo("image/jpeg")
    }
}
