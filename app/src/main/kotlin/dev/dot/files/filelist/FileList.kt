package dev.dot.files.filelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.dot.files.ui.theme.FilesTheme
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.isDirectory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun SelectableFileList(
    files: ImmutableList<Path>,
    selectedList: ImmutableList<Path>,
    isSelectionActive: Boolean,
    onEvent: (FileListUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Rational: the first long click should toggle the active selection mode.
    // Every other click should select or deselect an item.
    val onClick = { path: Path ->
        if (!isSelectionActive) {
            onEvent(FileListUiEvent.Open(path))
        } else {
            onEvent(FileListUiEvent.Select(path))
        }
    }

    val onSelect = { path: Path ->
        if (!isSelectionActive) {
            onEvent(FileListUiEvent.Select(path))
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn {
            stickyHeader {
                Surface {
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        item { Spacer(modifier = Modifier.width(8.dp)) }
                        (1..10).forEach { index ->
                            item {
                                Text(modifier = Modifier.padding(8.dp), text = "/")
                                Text(modifier = Modifier.padding(8.dp), text = "$index")
                            }
                        }
                        item { Spacer(modifier = Modifier.width(8.dp)) }
                    }
                }
            }

            items(
                items = files,
                key = { it.toString() },
            ) {
                var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
                var showRenameDialog by rememberSaveable { mutableStateOf(false) }

                SelectableListItem(
                    modifier = Modifier.animateItem(),
                    path = it,
                    onClick = onClick,
                    onSelect = onSelect,
                    isSelected = selectedList.contains(it),
                    menu = {
                        FileListMenu(
                            onRename = { showRenameDialog = true },
                            onDelete = { showDeleteDialog = true },
                            visible = !isSelectionActive,
                        )
                    },
                )

                if (showRenameDialog) {
                    RenameDialog(
                        path = it,
                        onConfirm = {
                            onEvent(FileListUiEvent.Rename(it))
                            showRenameDialog = false
                        },
                        onDismiss = { showRenameDialog = false },
                    )
                }
                if (showDeleteDialog) {
                    DeleteDialog(
                        path = it,
                        onConfirm = {
                            onEvent(FileListUiEvent.Delete(it))
                            showDeleteDialog = false
                        },
                        onDismiss = { showDeleteDialog = false },
                    )
                }
            }

            // This spacer allows the last item to be scrolled completely into the view, otherwise
            // the menu would be
            // partially hidden by the navigation bar and the FAB when scrolled to the bottom.
            item {
                Spacer(
                    modifier =
                        Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(bottom = FabAdjustments),
                )
            }
        }
        // TODO: Implement a scroll bar for the list
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SelectableListItem(
    path: Path,
    onClick: (Path) -> Unit,
    onSelect: (Path) -> Unit,
    isSelected: Boolean,
    menu: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDirectory = path.isDirectory()
    val backgroundColor by
        animateColorAsState(
            targetValue =
                if (isSelected) {
                    MaterialTheme.colorScheme.tertiaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
            animationSpec = tween(100),
            label = "Color",
        )

    val horizontalPadding by
        animateDpAsState(
            targetValue = if (isSelected) 8.dp else 0.dp,
            animationSpec = tween(100),
            label = "Horizontal padding",
        )
    val verticalPadding by
        animateDpAsState(
            targetValue = if (isSelected) 2.dp else 0.dp,
            animationSpec = tween(100),
            label = "Vertical padding",
        )

    val cornerRadius by
        animateDpAsState(
            targetValue = if (isSelected) 8.dp else 0.dp,
            animationSpec = tween(100),
            label = "Corner radius",
        )

    // FIXME: Height should be adjustable
    Surface(
        modifier = modifier.fillMaxWidth().height(64.dp),
    ) {
        Row(
            modifier =
                Modifier.padding(horizontal = horizontalPadding, vertical = verticalPadding)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(color = backgroundColor)
                    .combinedClickable(
                        onClick = { onClick(path) },
                        onLongClick = { onSelect(path) },
                    ),
        ) {
            // Icon box
            // TODO: Allow for previews of images and video thumbnails
            Box(modifier = Modifier.size(64.dp), contentAlignment = Alignment.Center) {
                if (!isDirectory) {
                    Icon(Icons.AutoMirrored.Outlined.InsertDriveFile, null)
                } else {
                    Icon(Icons.Outlined.FolderOpen, null)
                }
            }

            // File information
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = path.fileName.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // Menu Box
            Box(modifier = Modifier.size(64.dp), contentAlignment = Alignment.Center) { menu() }
        }
    }
}

@Composable
private fun FileListMenu(
    onRename: () -> Unit,
    onDelete: () -> Unit,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(tween(100)),
        exit = scaleOut(tween(100)),
    ) {
        var expanded by remember { mutableStateOf(false) }
        IconButton(modifier = modifier, onClick = { expanded = !expanded }) {
            Icon(Icons.Outlined.MoreVert, null)
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                // Rename and delete
                DropdownMenuItem(
                    text = { Text(text = "Rename") },
                    onClick = {
                        onRename()
                        expanded = false
                    },
                    leadingIcon = { Icon(Icons.Outlined.Create, null) },
                )
                DropdownMenuItem(
                    text = { Text(text = "Delete", color = Color.Red) },
                    onClick = {
                        onDelete()
                        expanded = false
                    },
                    leadingIcon = { Icon(Icons.Outlined.DeleteOutline, null, tint = Color.Red) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectableFileListPreview() {
    FilesTheme {
        Column {
            val list = (1..6).map { Path("File $it") }.toPersistentList()
            val selectedList = (5..6).map { Path("File $it") }.toPersistentList()
            SelectableFileList(
                files = list,
                selectedList = selectedList,
                isSelectionActive = false,
                onEvent = {},
            )
        }
    }
}

@Preview
@Composable
private fun SelectableListItemPreview() {
    FilesTheme {
        Column {
            val selected by remember { mutableStateOf(true) }
            SelectableListItem(
                path = Path("File 1"),
                onClick = {},
                onSelect = {},
                isSelected = !selected,
                menu = { FileListMenu(onRename = {}, onDelete = {}, true) },
            )
            SelectableListItem(
                path = Path("Folder"),
                onClick = {},
                onSelect = {},
                isSelected = !selected,
                menu = { FileListMenu(onRename = {}, onDelete = {}, true) },
            )
            SelectableListItem(
                path = Path("File"),
                onClick = {},
                onSelect = {},
                isSelected = selected,
                menu = { FileListMenu(onRename = {}, onDelete = {}, true) },
            )
        }
    }
}

private val FabAdjustments = 88.dp
