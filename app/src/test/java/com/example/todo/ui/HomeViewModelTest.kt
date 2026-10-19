@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.todo.ui

import com.example.todo.fakes.FakeTaskRepository
import com.example.todo.models.Task
import com.example.todo.ui.home.HomeUiState
import com.example.todo.ui.home.HomeViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addNewTask_returnSuccessWithOneTask() = runTest {
        val repo = FakeTaskRepository()
        val viewModel = HomeViewModel(repo)

        // Add Collector to allow emission
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        val testTask =  Task(
            title = "Test Task",
        )
        viewModel.addTask(testTask)

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is HomeUiState.Success)

        val tasks = (viewModel.uiState.value as HomeUiState.Success).tasks
        assertEquals(1, tasks.size)
        assertEquals(testTask, tasks.first())
    }

    @Test
    fun toggleCompletion_returnsSuccessWithUpdatedTask() = runTest {
        val repo = FakeTaskRepository(
            initialTasks = listOf(
                Task(
                    id = 1,
                    title = "Test Task",
                    completed = false
                )
            )
        )
        val viewModel = HomeViewModel(repo)

        // Add Collector to allow emission
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }

        viewModel.toggleCompleteTask(1)

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is HomeUiState.Success)

        val tasks = (viewModel.uiState.value as HomeUiState.Success).tasks
        assertEquals(true, tasks.first().completed)
    }
}

