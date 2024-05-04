package dev.dot.files.filelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Interests
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material.icons.outlined.RssFeed
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FileListDrawerLayout(
    drawerState: DrawerState,
    onEvent: (FileListUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerContent = { FileListDrawerSheet(onEvent) },
        drawerState = drawerState,
        content = content,
    )
}

@Composable
fun FileListDrawerSheet(
    onEvent: (FileListUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    isServerRunning: Boolean = false,
) {
    ModalDrawerSheet(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            // All 'mounted' paths, sometimes called places
            NavigationDrawerItem(
                label = { Text(text = "Home") },
                selected = true,
                onClick = {},
                icon = { Icon(Icons.Outlined.PhoneIphone, null) },
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // FTP-Server
            NavigationDrawerItem(
                label = { Text(text = "Share via FTP") },
                selected = false,
                onClick = { /*TODO*/ },
                icon = { Icon(Icons.Outlined.RssFeed, null) },
                badge = {
                    if (isServerRunning) {
                        // TODO: Show to indicate that the server is running
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            NavigationDrawerItem(
                label = { Text(text = "Add mount point") },
                selected = false,
                onClick = { /*TODO*/ },
                icon = { Icon(Icons.Outlined.Add, null) },
            )

            // Settings and about
            NavigationDrawerItem(
                label = { Text(text = "Settings") },
                selected = false,
                onClick = { /*TODO*/ },
                icon = { Icon(Icons.Outlined.Settings, null) },
            )
            NavigationDrawerItem(
                label = { Text(text = "About") },
                selected = false,
                onClick = { /*TODO*/ },
                icon = { Icon(Icons.Outlined.Interests, null) },
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DrawerPreview() {
    FileListDrawerLayout(
        drawerState = DrawerState(DrawerValue.Open),
        onEvent = {},
        content = {},
    )
}
