package com.example.todo.ui.task

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TaskRepository
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
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds


@OptIn(FlowPreview::class)
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val taskId: Long = checkNotNull(savedStateHandle["taskId"])
    val titleFieldState: TextFieldState = TextFieldState()
    val detailsFieldState: TextFieldState = TextFieldState()

    val currentTask = repository.getTask(taskId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    var autoSaveJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getTask(taskId).filterNotNull().first().let {
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
                    repository.updateTaskContent(taskId, title, details)
                    println("Autosave triggered: $title")
                }
        }
    }

    // Called on back
    suspend fun saveTask() {
        currentTask.value?.let { task ->
            repository.upsertTask(
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
        repository.deleteTask(taskId)
        return task
    }


    // Flush text and toggle complete state
    suspend fun toggleCompleted(): Boolean {
        currentTask.value?.let { task ->
            val isCompleted = task.completed
            repository.upsertTask(
                task.copy(
                    completed = !task.completed,
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
            repository.updateDueDate(taskId, dueDate)
        }
    }

    fun updateReminder(reminder: Reminder?) {
        viewModelScope.launch {
            repository.updateReminder(taskId, reminder)
        }
    }
}