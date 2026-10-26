package com.example.todo.ui.shared

import androidx.lifecycle.ViewModel
import com.example.todo.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

sealed interface TaskAction {
    data class DeleteTask(val task: Task): TaskAction
    data class CompleteTask(val taskId: Long): TaskAction
}

@HiltViewModel
class TaskActionViewModel @Inject constructor(
): ViewModel() {
    private val _taskAction = Channel<TaskAction>(capacity = 1)
    val taskActions = _taskAction.receiveAsFlow()

    fun publishDeletedTask(task: Task) {
        _taskAction.trySend(TaskAction.DeleteTask(task))
    }

    fun publishCompletedTask(taskId: Long) {
        _taskAction.trySend(TaskAction.CompleteTask(taskId))
    }
}