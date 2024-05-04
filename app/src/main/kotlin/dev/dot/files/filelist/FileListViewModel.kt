package dev.dot.files.filelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import dev.dot.files.FilesApp
import dev.dot.files.data.filesystem.FileSystemRepository
import dev.dot.files.data.preferences.PreferencesRepository
import dev.dot.files.data.preferences.SortOrder
import dev.dot.files.data.preferences.SortType
import java.nio.file.Path
import java.util.Stack
import kotlin.io.path.Path
import kotlin.io.path.fileSize
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.reflect.KClass
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class FileListViewModel(
    application: Application,
    private val fileSyStemRepository: FileSystemRepository,
    private val preferencesRepository: PreferencesRepository,
) : AndroidViewModel(application) {
    private val root = Path("/storage/emulated/0")

    // The current back stack
    private val stack = MutableStateFlow<Stack<Path>>(Stack())

    private val files = MutableStateFlow<List<Path>>(emptyList())

    private val selectedFiles = MutableStateFlow(persistentListOf<Path>())

    data class SortParameters(val sortOrder: SortOrder, val sortType: SortType)

    private val sortOrder = preferencesRepository.preferences.map { it.sortOrder }
    private val sortType = preferencesRepository.preferences.map { it.sortType }
    private val sortParameters =
        combine(sortOrder, sortType) { sortOrder, sortType ->
            SortParameters(sortOrder = sortOrder, sortType = sortType)
        }

    private val error = MutableStateFlow<Throwable?>(null)

    val uiState =
        combine(
                stack,
                files,
                selectedFiles,
                sortParameters,
            ) { stack, files, selectedFiles, sortParameters ->
                val path = getBackstackSave(stack, root)
                val sortedFiles = sortFiles(files, sortParameters).toImmutableList()
                val isSelectionActive = selectedFiles.isNotEmpty()
                FileListUiState(
                    path = path,
                    files = sortedFiles,
                    selectedFiles = selectedFiles,
                    isSelectionActive = isSelectionActive,
                    sortOrder = sortParameters.sortOrder,
                    sortType = sortParameters.sortType,
                    isRefreshing = false,
                    error = null,
                    usesOnBackPressedDispatcher = stack.size > 1,
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), FileListUiState())

    init {
        open(root)
    }

    private fun sortFiles(files: List<Path>, sortParameters: SortParameters) =
        files.sortedWith(
            if (sortParameters.sortOrder == SortOrder.Ascending) {
                when (sortParameters.sortType) {
                    SortType.Alphabetical -> compareBy { it }
                    SortType.LastModified -> compareBy { it.getLastModifiedTime() }
                    SortType.Length -> compareBy { it.fileName.toString().length }
                    SortType.Size -> compareBy { it.fileSize() }
                }
            } else {
                when (sortParameters.sortType) {
                    SortType.Alphabetical -> compareByDescending { it }
                    SortType.LastModified -> compareByDescending { it.getLastModifiedTime() }
                    SortType.Length -> compareByDescending { it.fileName.toString().length }
                    SortType.Size -> compareByDescending { it.fileSize() }
                }
            },
        )

    fun event(event: FileListUiEvent) {
        error.update { null }
        try {
            when (event) {
                is FileListUiEvent.CreateDirectory -> createDirectory(event.path)
                is FileListUiEvent.CreateFile -> createFile(event.path)
                is FileListUiEvent.Delete -> delete(event.path)
                is FileListUiEvent.OnBackPressed -> onBackPressed()
                is FileListUiEvent.OnMenuClicked -> onMenuClicked()
                is FileListUiEvent.Open -> open(event.path)
                is FileListUiEvent.Refresh -> refresh()
                is FileListUiEvent.Rename -> TODO()
                is FileListUiEvent.Select -> TODO()
                is FileListUiEvent.SortOrderChange -> sortOrderChange(event.order)
                is FileListUiEvent.SortTypeChange -> sortTypeChange(event.type)
                is FileListUiEvent.StartFtpServer -> startFtpServer()
            }
        } catch (e: Exception) {
            error.update {
                Timber.e(e)
                e
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            files.update { fileSyStemRepository.listFiles(getBackstackSave(stack.value, root)) }
        }
    }

    private fun createDirectory(path: String) {
        viewModelScope.launch {
            val directory = getBackstackSave(stack.value, root).resolve(path)
            if (!directory.isDirectory()) {
                return@launch
            }
            fileSyStemRepository.createDirectory(getBackstackSave(stack.value, root).resolve(path))
            files.update { it + directory }
        }
    }

    private fun createFile(path: String) {
        viewModelScope.launch {
            val file = getBackstackSave(stack.value, root).resolve(path)
            if (!file.isRegularFile()) {
                return@launch
            }
            fileSyStemRepository.createFile(file)
            files.update { it + file }
        }
    }

    private fun delete(path: Path) {
        viewModelScope.launch {
            fileSyStemRepository.delete(path)
            files.update { it - path }
        }
    }

    private fun onBackPressed() {
        viewModelScope.launch {
            stack.value.pop()
            refresh()
        }
    }

    private fun onMenuClicked() {
        viewModelScope.launch {}
    }

    private fun `open`(path: Path) {
        if (path.isDirectory()) {
            viewModelScope.launch {
                stack.update {
                    it.push(path)
                    it
                }
                refresh()
            }
        }
    }

    private fun sortOrderChange(order: SortOrder) {
        viewModelScope.launch {
            Timber.d("called")
            preferencesRepository.updateSortOrder(order)
        }
    }

    private fun sortTypeChange(type: SortType) {
        viewModelScope.launch { preferencesRepository.updateSortType(type) }
    }

    private fun startFtpServer() {}

    companion object {
        val Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: KClass<T>,
                    extras: CreationExtras,
                ): T {
                    val application = extras[APPLICATION_KEY] as FilesApp
                    return FileListViewModel(
                        application,
                        FileSystemRepository(),
                        PreferencesRepository(application.dataStore),
                    )
                        as T
                }
            }

        fun getBackstackSave(stack: Stack<Path>, default: Path): Path {
            return if (stack.size > 0) {
                stack.peek()
            } else {
                default
            }
        }
    }
}
