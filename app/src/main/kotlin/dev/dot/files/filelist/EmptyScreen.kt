package dev.dot.files.filelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.dot.files.ui.theme.FilesTheme

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(128.dp),
                imageVector = Icons.TwoTone.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
            )
            Text(text = "Nothing but dust here")
        }
    }
}

@Preview
@Composable
private fun EmptyScreenPreview() {
    FilesTheme { EmptyScreen() }
}
