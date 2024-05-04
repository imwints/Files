package dev.dot.files

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.dot.files.ui.theme.FilesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class TextViewerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val uri =
            intent.data
                ?: run {
                    Timber.e("No URI found in intent")
                    finish()
                    return
                }

        val viewModel =
            ViewModelProvider(
                this,
                TextViewerViewModelFactory(uri, contentResolver),
            )[TextViewerViewModel::class.java]

        viewModel.readText()

        setContent {
            FilesTheme {
                val state by viewModel.state.collectAsState()

                Scaffold {
                    Box(modifier = Modifier.padding(it)) {
                        state.let { uiState ->
                            when (uiState) {
                                is TextViewerState.Loading -> {
                                    Text(text = "Loading...")
                                }
                                is TextViewerState.Success -> {
                                    Text(
                                        text = uiState.text,
                                        modifier =
                                            Modifier.verticalScroll(
                                                rememberScrollState(),
                                                flingBehavior = ScrollableDefaults.flingBehavior(),
                                            ),
                                    )
                                }
                                is TextViewerState.Error -> {
                                    Text(text = uiState.message)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

sealed class TextViewerState {
    data object Loading : TextViewerState()

    data class Success(val text: String) : TextViewerState()

    data class Error(val message: String) : TextViewerState()
}

class TextViewerViewModel(
    private val uri: Uri,
    private val contentResolver: ContentResolver,
) : ViewModel() {
    private val _state = MutableStateFlow<TextViewerState>(TextViewerState.Loading)
    val state = _state.asStateFlow()

    fun readText() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                contentResolver.openInputStream(uri).use { inputStream ->
                    inputStream?.let {
                        val text = inputStream.readBytes().decodeToString()
                        _state.value = TextViewerState.Success(text)
                    } ?: run { _state.value = TextViewerState.Error("Failed to read text") }
                }
            }
        }
    }
}

class TextViewerViewModelFactory(
    private val uri: Uri,
    private val contentResolver: ContentResolver,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return TextViewerViewModel(uri, contentResolver) as T
    }
}
