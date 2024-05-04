package dev.dot.files.filelist

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.dot.files.BuildConfig
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlinx.coroutines.launch

// This deliberately takes a viewmodel as dependency. This is required to `collect` the state
@Composable
fun FileListScreen(
    viewModel: FileListViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = { event: FileListUiEvent ->
        when (event) {
            is FileListUiEvent.Open -> {
                if (event.path.isDirectory()) {
                    viewModel.event(event)
                } else {
                    openFile(event.path, context)
                }
            }
            else -> viewModel.event(event)
        }
    }
    FileListScreen(modifier = modifier, uiState = uiState, onEvent = onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FileListScreen(
    uiState: FileListUiState,
    onEvent: (FileListUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // The back handler will navigate up in the back stack, which is handled by the view model
    BackHandler(enabled = uiState.usesOnBackPressedDispatcher) {
        onEvent(FileListUiEvent.OnBackPressed)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    FileListDrawerLayout(
        modifier = modifier,
        drawerState = drawerState,
        onEvent = onEvent,
    ) {
        val scope = rememberCoroutineScope()
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        var showSortSheet by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                FileListTopBar(
                    uiState = uiState,
                    onEvent = onEvent,
                    onMenuClicked = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    },
                    onSortClicked = { showSortSheet = true },
                    scrollBehavior = scrollBehavior,
                )
            },
            floatingActionButton = {
                var isExpanded by rememberSaveable { mutableStateOf(false) }
                val onFabClick = { isExpanded = !isExpanded }
                var showNewFileDialog by rememberSaveable { mutableStateOf(false) }
                var showNewDirectoryDialog by rememberSaveable { mutableStateOf(false) }

                FileListFab(
                    isExpanded = isExpanded,
                    onClick = onFabClick,
                    onCreateFile = { showNewFileDialog = true },
                    onCreateFolder = { showNewDirectoryDialog = true },
                )

                if (showNewFileDialog) {
                    FileDialog(
                        onConfirm = { onEvent(FileListUiEvent.CreateFile(it)) },
                        onDismiss = {
                            showNewFileDialog = false
                            isExpanded = false
                        },
                    )
                }

                if (showNewDirectoryDialog) {
                    DirectoryDialog(
                        onConfirm = { onEvent(FileListUiEvent.CreateDirectory(it)) },
                        onDismiss = {
                            showNewDirectoryDialog = false
                            isExpanded = false
                        },
                    )
                }
            },
        ) { padding ->
            if (uiState.files.isNotEmpty()) {
                SelectableFileList(
                    modifier = Modifier.padding(top = padding.calculateTopPadding()),
                    files = uiState.files,
                    selectedList = uiState.selectedFiles,
                    isSelectionActive = uiState.isSelectionActive,
                    onEvent = onEvent,
                )
            }

            if (showSortSheet) {
                SortSheet(
                    onDismiss = { showSortSheet = false },
                    currentSortOrder = uiState.sortOrder,
                    currentSortType = uiState.sortType,
                    onSortOrderChange = { onEvent(FileListUiEvent.SortOrderChange(it)) },
                    onSortTypeChange = { onEvent(FileListUiEvent.SortTypeChange(it)) },
                    sheetState = sheetState,
                )
            }
        }
    }
}

private fun openFile(path: Path, context: Context) {
    val uri =
        FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.file.provider",
            path.toFile(),
        )
    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
}
