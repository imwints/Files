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
import kotlin.io.path.isDirectory
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewDirectoryWorkerTest {
    private lateinit var context: Context

    private lateinit var realDirectoryPath: Path

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        realDirectoryPath = Path(context.cacheDir.path + "/test")
    }

    @Test
    fun `test NewDirectoryWorker creates new directory`() {
        assertThat(realDirectoryPath.exists()).isFalse()

        val data = workDataOf(NewDirectoryWorker.KEY_DIRECTORY_PATH to realDirectoryPath.toString())
        val worker =
            TestListenableWorkerBuilder<NewDirectoryWorker>(context).setInputData(data).build()
        val result = worker.startWork().get()

        assertThat(result).isEqualTo(ListenableWorker.Result.success())

        assertThat(realDirectoryPath.exists()).isTrue()
        assertThat(realDirectoryPath.isDirectory()).isTrue()
    }

    @Test
    fun `test NewDirectoryWorker fails to create with invalid path`() {
        val path = "/super/nice/root/dir" + "/test"
        val data = workDataOf(NewDirectoryWorker.KEY_DIRECTORY_PATH to path)
        val worker =
            TestListenableWorkerBuilder<NewDirectoryWorker>(context).setInputData(data).build()
        val result = worker.startWork().get()

        assertThat(result).isEqualTo(ListenableWorker.Result.failure())
    }

    @Test
    fun `test NewDirectoryWorker fails to create in protected directory`() {
        val path = "/system" + "/test"
        val data = workDataOf(NewDirectoryWorker.KEY_DIRECTORY_PATH to path)
        val worker =
            TestListenableWorkerBuilder<NewDirectoryWorker>(context).setInputData(data).build()
        val result = worker.startWork().get()

        assertThat(result).isEqualTo(ListenableWorker.Result.failure())
    }

    @Test(expected = ExecutionException::class)
    fun `test NewDirectoryWorker throws exception with relative path`() {
        val path = "test"
        val data = workDataOf(NewDirectoryWorker.KEY_DIRECTORY_PATH to path)
        val worker =
            TestListenableWorkerBuilder<NewDirectoryWorker>(context).setInputData(data).build()
        worker.startWork().get()
    }

    @After
    fun tearDown() {
        realDirectoryPath.deleteIfExists()
    }
}
