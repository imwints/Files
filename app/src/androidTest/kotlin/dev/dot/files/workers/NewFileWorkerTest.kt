package dev.dot.files.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.google.common.truth.Truth.assertThat
import java.nio.file.Path
import java.util.concurrent.ExecutionException
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewFileWorkerTest {
    private lateinit var context: Context

    private lateinit var realFilePath: Path

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        realFilePath = Path(context.cacheDir.path + "/test.txt")
    }

    @Test
    fun `test NewFileWorker creates new file`() {
        assertThat(realFilePath.exists()).isFalse()

        val data = workDataOf(NewFileWorker.KEY_FILE_PATH to realFilePath.toString())
        val worker = TestListenableWorkerBuilder<NewFileWorker>(context).setInputData(data).build()
        val result = worker.startWork().get()

        assertThat(result).isEqualTo(ListenableWorker.Result.success())

        assertThat(realFilePath.exists()).isTrue()
        assertThat(realFilePath.isRegularFile()).isTrue()
    }

    @Test
    fun `test NewFileWorker fails to create with invalid path`() {
        val path = "/super/nice/root/dir" + "/test.txt"
        val data = workDataOf(NewFileWorker.KEY_FILE_PATH to path)
        val worker = TestListenableWorkerBuilder<NewFileWorker>(context).setInputData(data).build()
        val result = worker.startWork().get()

        assertThat(result).isEqualTo(ListenableWorker.Result.failure())
    }

    @Test
    fun `test NewFileWorker fails to create in protected directory`() {
        val path = "/system" + "/test.txt"
        val data = workDataOf(NewFileWorker.KEY_FILE_PATH to path)
        val worker = TestListenableWorkerBuilder<NewFileWorker>(context).setInputData(data).build()
        val result = worker.startWork().get()

        assertThat(result).isEqualTo(ListenableWorker.Result.failure())
    }

    @Test(expected = ExecutionException::class)
    fun `test NewFileWorker throws exception with relative path`() {
        val path = "test.txt"
        val data = workDataOf(NewFileWorker.KEY_FILE_PATH to path)
        val worker = TestListenableWorkerBuilder<NewFileWorker>(context).setInputData(data).build()
        worker.startWork().get()
    }

    @After
    fun teardown() {
        realFilePath.deleteIfExists()
    }
}
