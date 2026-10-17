package com.example.todo.ui

import com.example.todo.fakes.FakeTaskRepository
import com.example.todo.ui.home.HomeViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.junit.rules.TestWatcher

class HomeViewModelTest {
    val repo = FakeTaskRepository()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun addNewTask_andToggleCompletion() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val viewModel = HomeViewModel(FakeTaskRepository())

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.tasks.collect()
        }

        viewModel.addNewTask("Test Task")
        advanceUntilIdle()
        val task1 = viewModel.tasks.value.first()

        // Assert against the actual state flow value directly
        assertEquals("Test Task", task1.title)
        assertEquals(false, task1.completed)

        viewModel.toggleCompleteTask(task1, true)
        val task2 = viewModel.tasks.value.first()
        assertEquals(true, task2.completed)

    }
}