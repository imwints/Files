package dev.dot.files.filelist

import androidx.compose.runtime.Immutable
import dev.dot.files.data.preferences.SortOrder
import dev.dot.files.data.preferences.SortType
import java.nio.file.Path
import kotlin.io.path.Path
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FileListUiState(
    val path: Path = Path(""),
    val files: ImmutableList<Path> = persistentListOf(),
    val selectedFiles: ImmutableList<Path> = persistentListOf(),
    val isSelectionActive: Boolean = false,
    val sortOrder: SortOrder = SortOrder.Ascending,
    val sortType: SortType = SortType.Alphabetical,
    val isRefreshing: Boolean = false,
    val usesOnBackPressedDispatcher: Boolean = false,
    val error: Throwable? = null,
)
