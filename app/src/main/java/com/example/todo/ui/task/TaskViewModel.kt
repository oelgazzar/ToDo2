package com.example.todo.ui.task

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.CategoryRepository
import com.example.todo.data.TaskRepository
import com.example.todo.models.Category
import com.example.todo.models.Reminder
import com.example.todo.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds


@OptIn(FlowPreview::class)
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val taskId: Long = checkNotNull(savedStateHandle["taskId"])
    val titleFieldState: TextFieldState = TextFieldState()
    val detailsFieldState: TextFieldState = TextFieldState()

    val currentTask = taskRepository.getTask(taskId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val categories = categoryRepository.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var autoSaveJob: Job? = null

    init {
        viewModelScope.launch {
            taskRepository.getTask(taskId).filterNotNull().first().let {
                titleFieldState.setTextAndPlaceCursorAtEnd(it.title)
                detailsFieldState.setTextAndPlaceCursorAtEnd(it.details.orEmpty())
            }
        }

        // Autosave
        autoSaveJob = viewModelScope.launch {
            combine(
                snapshotFlow { titleFieldState.text.toString() },
                snapshotFlow { detailsFieldState.text.toString() },
            ) { title, details ->
                title to details
            }
                .drop(1)
                .debounce(500.milliseconds)
                .collect { (title, details) ->
                    taskRepository.updateTaskContent(taskId, title, details)
                }
        }
    }

    // Called on back
    suspend fun saveTask() {
        currentTask.value?.let { task ->
            taskRepository.upsertTask(
                task.copy(
                    title = titleFieldState.text.toString(),
                    details = detailsFieldState.text.toString().ifBlank { null }
                ))
        }
    }

    suspend fun deleteTask(): Task? {
        autoSaveJob?.cancel()
        val task = currentTask.value?.copy(
            title = titleFieldState.text.toString(),
            details = detailsFieldState.text.toString().ifBlank { null }
        )
        taskRepository.deleteTask(taskId)
        return task
    }


    // Flush text and toggle complete state
    suspend fun toggleCompleted(): Boolean {
        currentTask.value?.let { task ->
            val isCompleted = task.completedAt != null
            taskRepository.upsertTask(
                task.copy(
                    completedAt = if (isCompleted) null else LocalDate.now(),
                    // Flush text
                    title = titleFieldState.text.toString(),
                    details = detailsFieldState.text.toString().ifBlank { null }
                )
            )
            return !isCompleted
        }
        return false
    }

    fun updateDueDate(dueDate: LocalDate?) {
        viewModelScope.launch {
            taskRepository.updateDueDate(taskId, dueDate)
        }
    }

    fun updateReminder(reminder: Reminder?) {
        viewModelScope.launch {
            taskRepository.updateReminder(taskId, reminder)
        }
    }

    fun removeRepeat() {
        viewModelScope.launch {
            taskRepository.removeRepeat(taskId)
        }
    }

    fun onSelectCategory(categoryId: Long?) {
        viewModelScope.launch {
            taskRepository.updateCategory(taskId, categoryId)
        }
    }

    fun onCreateCategory(name: String) {
        viewModelScope.launch {
            val categoryId = categoryRepository.upsertCategory(
                Category(
                    name = name,
                )
            )
            onSelectCategory(categoryId)
        }
    }

    fun toggleStarred() {
        viewModelScope.launch {
            taskRepository.toggleStarred(taskId)
        }
    }
}