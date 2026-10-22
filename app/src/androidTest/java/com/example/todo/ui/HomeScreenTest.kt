package com.example.todo.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.todo.TestActivity
import com.example.todo.data.TaskRepository
import com.example.todo.models.Schedule
import com.example.todo.models.Task
import com.example.todo.ui.home.HomeScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import javax.inject.Inject

@HiltAndroidTest
class HomeScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    @Inject
    lateinit var repo: TaskRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun addTaskButton_displaysTask() = runTest{
        composeTestRule.setContent {
            HomeScreen(
                navigateToTaskScreen = {},
                navigateToRepeatFormScreen = {}
            )
        }
        assertEquals(0, repo.getAllTasks().first().size)
        composeTestRule.apply {
            onNodeWithTag("add_task_fab").performClick()
            onNodeWithText("new task", ignoreCase = true).performTextInput("Task title")
            onNodeWithText("Save").performClick()
            onNodeWithText("Task title").assertIsDisplayed()
            assertEquals("Task title", repo.getAllTasks().first().first().title)
        }
    }

    @Test
    fun checkTask_makesTaskCompleted() = runTest {
        repo.upsertTask(Task(
            title = "Task title",
            details = "Task details",
            position = -1
        ))

        composeTestRule.setContent {
            HomeScreen({}, {})
        }
        composeTestRule.apply {
            onNodeWithTag("task_completed_checkbox").performClick()
            assertEquals(true, repo.getAllTasks().first().first().completed)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun task_showsDueDateProperly() = runTest{
        repo.upsertTask(Task(
            title = "Task title",
            details = "Task details",
            schedule = Schedule(dueDate = LocalDate.of(2026, 10, 20)),
            position = -1
        ))

        composeTestRule.setContent {
            HomeScreen({}, {})
        }
        composeTestRule.apply {
            onNodeWithText("Due Today").assertIsDisplayed()
        }
    }
}

