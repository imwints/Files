package dev.dot.files

import android.app.Application
import androidx.datastore.dataStore
import dev.dot.files.data.preferences.PreferencesSerializer
import timber.log.Timber

// TODO: Remove
class FilesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    val dataStore by dataStore("preferences.json", PreferencesSerializer)
}
