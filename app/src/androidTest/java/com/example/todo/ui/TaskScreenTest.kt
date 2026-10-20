package com.example.todo.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isEditable
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.todo.MainActivity
import com.example.todo.data.TaskRepository
import com.example.todo.data.today
import com.example.todo.models.Task
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltAndroidTest
class TaskScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var repo: TaskRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun editTask_updatesTaskInHomeScreen() = runTest {
        repo.upsertTask(
            Task(
                title = "Task title",
                details = "Task details",
                completed = false
            )
        )
        composeTestRule.apply {
            onNodeWithText("Task title").performClick()
            onNodeWithText("Task title").performTextClearance()
            onNodeWithText("Enter title", ignoreCase = true).performTextInput("Updated title")
            onNodeWithTag("back_button").performClick()
            onNodeWithText("Updated title").assertIsDisplayed()
        }
    }

    @Test
    fun deleteTask_showsProperMessageWithUndoAction() = runTest {
        repo.upsertTask(
            Task(
                title = "Task title",
                details = "Task details",
                completed = false
            )
        )
        composeTestRule.apply {
            onNodeWithText("Task title").performClick()
            onNodeWithTag("menu_button").performClick()
            onNodeWithText("Delete").performClick()
            onNodeWithText("Yes", ignoreCase = true).performClick()
            onNodeWithText("Task deleted", ignoreCase = true).assertIsDisplayed()
            onNodeWithText("Undo").assertIsDisplayed()
        }
    }

    @Test
    fun undoTask_readdTask() = runTest {
        repo.upsertTask(
            Task(
                title = "Task title",
                details = "Task details",
                completed = false
            )
        )
        composeTestRule.apply {
            onNodeWithText("Task title").performClick()
            onNodeWithTag("menu_button").performClick()
            onNodeWithText("Delete").performClick()
            onNodeWithText("Yes", ignoreCase = true).performClick()
        }

        assertEquals(0, repo.getAllTasks().first().size)

        composeTestRule.apply {
            onNodeWithText("Undo").performClick()
        }

        assertEquals(1, repo.getAllTasks().first().size)
    }

    @Test
    fun toggleCompleteTask_showsProperMessageWithUndoAction() = runTest {
        repo.upsertTask(
            Task(
                title = "Task title",
                details = "Task details",
                completed = false
            )
        )
        composeTestRule.apply {
            onNodeWithText("Task title").performClick()
            onNodeWithText("mark completed", ignoreCase = true).performClick()
            onNodeWithText("Task marked completed", ignoreCase = true).assertIsDisplayed()
            onNodeWithText("Undo").assertIsDisplayed()
        }
    }

    @Test
    fun undoCompleteTask_resetsTask() = runTest {
        repo.upsertTask(
            Task(
                title = "Task title",
                details = "Task details",
                completed = false
            )
        )
        composeTestRule.apply {
            onNodeWithText("Task title").performClick()
            onNodeWithText("mark completed", ignoreCase = true).performClick()
        }

        assertEquals(true, repo.getAllTasks().first().first().completed)

        composeTestRule.apply {
            onNodeWithText("Undo").performClick()
        }

        assertEquals(false, repo.getAllTasks().first().first().completed)
    }
    @Test
    fun addDeadline_showsInHomeScreen() = runTest {
        val today = LocalDate.now().minusDays(1)
        repo.upsertTask(
            Task(
                title = "Task title",
                details = "Task details",
            )
        )
        composeTestRule.apply {
            onNodeWithText("Task title").performClick()
            onNodeWithText("Add deadline", ignoreCase = true).performClick()
            onNodeWithContentDescription("Switch to text input mode").performClick()
            onNodeWithContentDescription("Date, MM/DD/YYYY")
                .performTextInput(DateTimeFormatter.ofPattern("MMddYYYY").format(today))
            onNodeWithText("Ok").performClick()
            onNodeWithTag("back_button").performClick()
            onNodeWithText("Due Yesterday").assertIsDisplayed()
        }
    }

}
