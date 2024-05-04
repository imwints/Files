package dev.dot.files.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PreferencesRepositoryTest {
    private val testContext: Context = ApplicationProvider.getApplicationContext()

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    private val testScope: TestScope = TestScope(context = testDispatcher + Job())

    private val testDataStore: DataStore<Preferences> =
        DataStoreFactory.create(
            serializer = PreferencesSerializer,
            scope = testScope,
            produceFile = { testContext.dataStoreFile("user_preferences.json") },
        )

    private val repository: PreferencesRepository = PreferencesRepository(testDataStore)

    @Test fun repository_testFetchInitialPreferences() {}

    @Test fun repository_testWriteSortOrderBySize() {}
}
