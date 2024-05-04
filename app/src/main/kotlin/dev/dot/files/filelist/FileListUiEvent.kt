package dev.dot.files.filelist

import dev.dot.files.data.preferences.SortOrder
import dev.dot.files.data.preferences.SortType
import java.nio.file.Path

sealed class FileListUiEvent {
    data class CreateDirectory(val path: String) : FileListUiEvent()

    data class CreateFile(val path: String) : FileListUiEvent()

    data class Delete(val path: Path) : FileListUiEvent()

    data object OnBackPressed : FileListUiEvent()

    data object OnMenuClicked : FileListUiEvent()

    data class Open(val path: Path) : FileListUiEvent()

    data object Refresh : FileListUiEvent()

    data class Rename(val path: String) : FileListUiEvent()

    data class Select(val path: Path) : FileListUiEvent()

    data class SortTypeChange(val type: SortType) : FileListUiEvent()

    data class SortOrderChange(val order: SortOrder) : FileListUiEvent()

    data object StartFtpServer : FileListUiEvent()
}
