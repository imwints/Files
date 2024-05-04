package dev.dot.files.filelist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.dot.files.ui.theme.FilesTheme

// Fab that expands to show two more fabs for creating files and folders
@Composable
internal fun FileListFab(
    isExpanded: Boolean,
    onClick: () -> Unit,
    onCreateFile: () -> Unit,
    onCreateFolder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color by
        animateColorAsState(
            targetValue =
                if (isExpanded) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                },
            animationSpec = tween(250),
            label = "FAB color",
        )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column {
            AnimatedVisibility(
                visible = isExpanded,
                enter =
                    slideInHorizontally(
                        animationSpec = tween(delayMillis = 100),
                        initialOffsetX = { 200 },
                    ),
                exit = slideOutHorizontally(animationSpec = tween(), targetOffsetX = { 200 }),
            ) {
                SmallFloatingActionButton(
                    onClick = onCreateFile,
                    content = {
                        Icon(
                            Icons.AutoMirrored.Outlined.InsertDriveFile,
                            contentDescription = "Create file",
                        )
                    },
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInHorizontally(animationSpec = tween(), initialOffsetX = { 200 }),
                exit =
                    slideOutHorizontally(
                        animationSpec = tween(delayMillis = 100),
                        targetOffsetX = { 200 },
                    ),
            ) {
                SmallFloatingActionButton(
                    onClick = onCreateFolder,
                    content = {
                        Icon(Icons.Outlined.FolderOpen, contentDescription = "Create folder")
                    },
                )
            }
        }

        FloatingActionButton(
            onClick = onClick,
            containerColor = color,
            content = {
                //        val angle by animateFloatAsState(
                //          targetValue = if (isExpanded) -135f else 0f,
                //          label = "FAB Icon rotation"
                //        )
                AnimatedContent(
                    targetState = !isExpanded,
                    label = "FAB Icon",
                    transitionSpec = {
                        // This will animate the incoming icon to "kick" out the other icon
                        val offset = 200
                        if (targetState) {
                            slideInHorizontally(
                                    animationSpec = tween(),
                                    initialOffsetX = { -offset },
                                )
                                .togetherWith(
                                    slideOutHorizontally(
                                        animationSpec = tween(delayMillis = 100),
                                        targetOffsetX = { offset },
                                    ) +
                                        fadeOut(
                                            animationSpec = tween(delayMillis = 100),
                                            targetAlpha = 50f,
                                        ),
                                )
                        } else {
                            slideInHorizontally(
                                    animationSpec = tween(),
                                    initialOffsetX = { offset },
                                )
                                .togetherWith(
                                    slideOutHorizontally(
                                        animationSpec = tween(delayMillis = 100),
                                        targetOffsetX = { -offset },
                                    ) +
                                        fadeOut(
                                            animationSpec = tween(delayMillis = 100),
                                            targetAlpha = 50f,
                                        ),
                                )
                        }
                    },
                ) { visible ->
                    if (visible) {
                        Icon(Icons.Outlined.Add, "Add")
                    } else {
                        Icon(Icons.Outlined.Close, "Close")
                    }
                }
            },
        )
    }
}

@Preview
@Composable
private fun Preview() {
    FilesTheme {
        FileListFab(isExpanded = false, onClick = {}, onCreateFile = {}, onCreateFolder = {})
    }
}
