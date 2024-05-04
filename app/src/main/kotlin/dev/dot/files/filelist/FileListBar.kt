package dev.dot.files.filelist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.dot.files.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileListTopBar(
    uiState: FileListUiState,
    onEvent: (FileListUiEvent) -> Unit,
    onMenuClicked: () -> Unit,
    onSortClicked: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    isMenuExtended: Boolean = false,
) {
    val isSelectionActive = uiState.isSelectionActive
    TopAppBar(
        modifier = modifier,
        title = {
            // TODO: animate title change
            if (!isSelectionActive) {
                Text(text = stringResource(id = R.string.app_name))
            } else {
                Text(text = "${uiState.selectedFiles.size} selected")
            }
        },
        // TODO: animate actions change
        // FIXME: Check if there should be two buttons with two onClicks
        navigationIcon = {
            if (!isSelectionActive) {
                IconButton(onClick = onMenuClicked) { Icon(Icons.Outlined.Menu, "Menu") }
            } else {
                IconButton(onClick = { onEvent(FileListUiEvent.OnMenuClicked) }) {
                    Icon(Icons.Outlined.Close, "Clear selection")
                }
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) { Icon(Icons.Default.Search, "Search") }
            IconButton(onClick = onSortClicked) { Icon(Icons.AutoMirrored.Default.Sort, "Sort") }

            var expanded by rememberSaveable { mutableStateOf(isMenuExtended) }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Outlined.MoreVert, "More")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text(text = "Refresh") },
                        onClick = {
                            onEvent(FileListUiEvent.Refresh)
                            expanded = false
                        },
                        leadingIcon = { Icon(Icons.Outlined.Refresh, null) },
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
