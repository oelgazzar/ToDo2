@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.todo.ui

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshots.Snapshot.Companion.withMutableSnapshot
import androidx.lifecycle.SavedStateHandle
import com.example.todo.fakes.FakeTaskRepository
import com.example.todo.models.Task
import com.example.todo.ui.task.TaskViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class TaskViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val task = Task(
        id = 1L,
        title = "Original title",
        details = "Original details",
        completed = false
    )

    private lateinit var repository: FakeTaskRepository
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        repository = FakeTaskRepository(initialTasks = listOf(task))

        val savedStateHandle = SavedStateHandle(
            mapOf("taskId" to task.id)
        )

        viewModel = TaskViewModel(repository, savedStateHandle)
    }

    private fun TestScope.collectCurrentTask() {
        backgroundScope.launch(
            UnconfinedTestDispatcher(testScheduler)
        ) {
            viewModel.currentTask.collect()
        }
    }

    @Test
    fun `loads task title and details into text fields`() = runTest(
        mainDispatcherRule.testDispatcher
    ) {
        collectCurrentTask()

        assertEquals(
            "Original title",
            viewModel.titleFieldState.text.toString()
        )
        assertEquals(
            "Original details",
            viewModel.detailsFieldState.text.toString()
        )
    }

    @Test
    fun `autoSaves text after debounce`() = runTest(
        mainDispatcherRule.testDispatcher
    ) {
        withMutableSnapshot {
            viewModel.titleFieldState.setTextAndPlaceCursorAtEnd("Updated title")
        }

        advanceTimeBy(499.milliseconds)
        runCurrent()
        assertEquals(
            "Original title",
            repository.getTask(1).first()?.title
        )

        advanceTimeBy(1.milliseconds)
        runCurrent()
        assertEquals(
            "Updated title",
            repository.getTask(1L).first()?.title
        )
    }

    @Test
    fun `saveTask persists current text`() = runTest(
        mainDispatcherRule.testDispatcher
    ) {
        collectCurrentTask()
        runCurrent()

        viewModel.titleFieldState.edit {
            replace(0, length, "Saved title")
        }
        viewModel.detailsFieldState.edit {
            replace(0, length, "   ")
        }

        viewModel.saveTask()

        val saved = repository.getTask(1L).first()

        assertEquals("Saved title", saved?.title)
        assertNull(saved?.details)
    }

    @Test
    fun `toggleCompleted completes task and saves text`() = runTest(
        mainDispatcherRule.testDispatcher
    ) {
        collectCurrentTask()
        runCurrent()

        viewModel.titleFieldState.edit {
            replace(0, length, "Updated title")
        }

        val result = viewModel.toggleCompleted()

        val updated = repository.getTask(1L).first()

        assertTrue(result)
        assertTrue(updated!!.completed)
        assertEquals("Updated title", updated.title)
    }

    @Test
    fun `deleteTask deletes task and returns it`() = runTest(
        mainDispatcherRule.testDispatcher
    ) {
        collectCurrentTask()
        runCurrent()

        viewModel.titleFieldState.edit {
            replace(0, length, "Updated title")
        }

        val result = viewModel.deleteTask()

        val updated = repository.getTask(1L).first()

        assertNotNull(result)
        assertEquals("Updated title", result?.title)
        assertNull(updated)
    }
}
