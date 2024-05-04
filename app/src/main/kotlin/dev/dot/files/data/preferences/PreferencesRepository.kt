package dev.dot.files.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import timber.log.Timber

class PreferencesRepository(private val dataStore: DataStore<Preferences>) {
    val preferences =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading user preferences")
                emit(
                    Preferences(
                        showHidden = false,
                        sortOrder = SortOrder.Ascending,
                        sortType = SortType.Alphabetical,
                    ),
                )
            } else {
                throw exception
            }
        }

    suspend fun getSortOrder(): SortOrder {
        return dataStore.data.first().sortOrder
    }

    suspend fun getSortType(): SortType {
        return dataStore.data.first().sortType
    }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.updateData { it.copy(sortOrder = sortOrder) }
    }

    suspend fun updateSortType(sortType: SortType) {
        dataStore.updateData { it.copy(sortType = sortType) }
    }

    suspend fun getShowHidden(): Boolean {
        return dataStore.data.first().showHidden
    }

    suspend fun toggleShowHidden() {
        dataStore.updateData { it.copy(showHidden = !it.showHidden) }
    }

    suspend fun fetchInitialPreferences() = dataStore.data.first()
}
