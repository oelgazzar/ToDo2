package com.example.todo.ui.task

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.todo.data.TaskRepository
import com.example.todo.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val taskId = checkNotNull(savedStateHandle.toRoute<TaskDetails>().taskId)
    val titleFieldState: TextFieldState = TextFieldState()
    val detailsFieldState: TextFieldState = TextFieldState()

//    val taskDraft = combine(
//        repository.getTask(taskId).filterNotNull(),
//        snapshotFlow { titleFieldState.text.toString() },
//        snapshotFlow { detailsFieldState.text.toString() },
//    ) { task, title, details ->
//        task.copy(
//            title = title,
//            details = details
//        )
//    }
//        .stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000),
//        Task()
//    )

    init {
        viewModelScope.launch {
            repository.getTask(taskId).filterNotNull().first().let {
                titleFieldState.setTextAndPlaceCursorAtEnd(it.title)
                detailsFieldState.setTextAndPlaceCursorAtEnd(it.details.orEmpty())
            }
        }
    }

    suspend fun saveTask() {
        repository.upsertTask(Task(
            id = taskId,
            title = titleFieldState.text.toString(),
            details = detailsFieldState.text.toString().takeIf { it.isNotBlank() }
        ))
    }

    suspend fun deleteTask(): String {
        repository.deleteTask(taskId)
        val serializedTask = Json.encodeToString(
            Task(
                id = taskId,
                title = titleFieldState.text.toString(),
                details = detailsFieldState.text.toString().takeIf { it.isNotBlank() }
            )
        )
        return serializedTask
    }
}