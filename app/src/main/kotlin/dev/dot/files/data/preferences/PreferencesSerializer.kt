package dev.dot.files.data.preferences

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object PreferencesSerializer : Serializer<Preferences> {
    override val defaultValue: Preferences =
        Preferences(
            showHidden = false,
            sortOrder = SortOrder.Ascending,
            sortType = SortType.Alphabetical,
        )

    override suspend fun readFrom(input: InputStream): Preferences {
        try {
            return Json.decodeFromString(
                Preferences.serializer(),
                input.readBytes().decodeToString(),
            )
        } catch (e: SerializationException) {
            throw CorruptionException("Cannot read json.", e)
        }
    }

    override suspend fun writeTo(
        t: Preferences,
        output: OutputStream,
    ) {
        output.write(Json.encodeToString(Preferences.serializer(), t).encodeToByteArray())
    }
}
