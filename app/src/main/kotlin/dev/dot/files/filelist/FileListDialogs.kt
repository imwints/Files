package dev.dot.files.filelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.NoteAdd
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.TextRotationNone
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.twotone.CreateNewFolder
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.dot.files.data.preferences.SortOrder
import dev.dot.files.data.preferences.SortType
import java.nio.file.Path
import kotlin.io.path.Path

@Composable
fun DeleteDialog(
    path: Path,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val underlinedFileName =
        AnnotatedString.Builder("Are you sure you want to delete ")
            .apply {
                pushStyle(
                    SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        fontStyle = FontStyle.Italic,
                    ),
                )
                append(path.fileName.toString())
                pop()
                append("?")
                append("\n\nThis cannot be undone.")
            }
            .toAnnotatedString()

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors =
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            ) {
                Text("Delete")
            }
        },
        dismissButton = { FilledTonalButton(onClick = onDismiss) { Text("Cancel") } },
        icon = { Icon(Icons.TwoTone.Delete, null, modifier = Modifier.size(32.dp)) },
        title = { Text("Delete ${path.fileName}?") },
        text = {
            Text(
                underlinedFileName,
                textAlign = TextAlign.Center,
            )
        },
    )
}

@Preview
@Composable
private fun DeleteButtonPreview() {
    DeleteDialog(Path("test.txt"), {}, {})
}

@Composable
fun DirectoryDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(text)
                    onDismiss()
                },
            ) {
                Text("Create")
            }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancel") } },
        icon = { Icon(Icons.TwoTone.CreateNewFolder, null, modifier = Modifier.size(32.dp)) },
        title = { Text("Create a new directory") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = "Directory") },
            )
        },
    )
}

@Preview
@Composable
private fun DirectoryDialogPreview() {
    DirectoryDialog({}, {})
}

@Composable
fun FileDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(text)
                    onDismiss()
                },
            ) {
                Text("Create")
            }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancel") } },
        icon = { Icon(Icons.AutoMirrored.TwoTone.NoteAdd, null, modifier = Modifier.size(32.dp)) },
        title = { Text("Create a new file") },
        text = {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(text = "File") },
            )
        },
    )
}

@Preview
@Composable
private fun FileDialogPreview() {
    FileDialog({}, {})
}

@Composable
fun RenameDialog(
    path: Path,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onConfirm(text) },
            ) {
                Text(text = "Rename")
            }
        },
        dismissButton = { FilledTonalButton(onClick = onDismiss) { Text(text = "Cancel") } },
        icon = { Icon(Icons.TwoTone.Edit, null) },
        title = { Text(text = "Rename") },
        text = {
            Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = {
                        Text(
                            text = "${path.fileName}",
                            fontStyle = FontStyle.Italic,
                            textDecoration = TextDecoration.Underline,
                        )
                    },
                    placeholder = { Text(text = "To") },
                )
            }
        },
    )
}

@Preview
@Composable
private fun RenameDialogPreview() {
    RenameDialog(Path("test.txt"), {}, {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortSheet(
    onDismiss: () -> Unit,
    currentSortOrder: SortOrder,
    currentSortType: SortType,
    onSortOrderChange: (SortOrder) -> Unit,
    onSortTypeChange: (SortType) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column {
            SortSheetItem(
                name = "Ascending",
                isSelected = currentSortOrder == SortOrder.Ascending,
                icon = Icons.Default.ArrowDownward,
                onClick = { onSortOrderChange(SortOrder.Ascending) },
            )
            SortSheetItem(
                name = "Descending",
                isSelected = currentSortOrder == SortOrder.Descending,
                icon = Icons.Default.ArrowUpward,
                onClick = { onSortOrderChange(SortOrder.Descending) },
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

            SortSheetItem(
                name = "Alphabetical",
                isSelected = currentSortType == SortType.Alphabetical,
                icon = Icons.Default.SortByAlpha,
                onClick = { onSortTypeChange(SortType.Alphabetical) },
            )
            SortSheetItem(
                name = "Last modified",
                isSelected = currentSortType == SortType.LastModified,
                icon = Icons.Default.AccessTimeFilled,
                onClick = { onSortTypeChange(SortType.LastModified) },
            )
            SortSheetItem(
                name = "Length",
                isSelected = currentSortType == SortType.Length,
                icon = Icons.Default.TextRotationNone,
                onClick = { onSortTypeChange(SortType.Length) },
            )
            SortSheetItem(
                name = "Size",
                isSelected = currentSortType == SortType.Size,
                icon = Icons.Default.DataUsage,
                onClick = { onSortTypeChange(SortType.Size) },
            )
        }
    }
}

@Composable
private fun SortSheetItem(
    name: String,
    isSelected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.padding(horizontal = 16.dp),
            imageVector = icon,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = name,
        )
        if (isSelected) {
            Icon(
                modifier = Modifier.padding(horizontal = 16.dp),
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint,
            )
        }
    }
}

@Preview
@Composable
private fun SortSheetItemPreview() {
    Surface {
        SortSheetItem(
            name = "Ascending",
            isSelected = true,
            icon = Icons.Outlined.FilterAlt,
            onClick = {},
        )
    }
}
