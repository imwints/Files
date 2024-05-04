package dev.dot.files.filelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dev.dot.files.ui.theme.FilesTheme

class FileListActivity : ComponentActivity() {
    private val viewModel by viewModels<FileListViewModel> { FileListViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { FilesTheme { FileListScreen(viewModel) } }
    }
}
