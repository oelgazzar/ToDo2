package com.example.todo.ui.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.todo.models.Task
import com.example.todo.ui.shared.TaskAction

@Composable
fun TaskActionEffect(
    taskAction: TaskAction?,
    snackBarState: SnackbarHostState,
    onUndoDelete: (Task) -> Unit,
    onUndoComplete: (Long) -> Unit,
) {
    LaunchedEffect(taskAction) {
        taskAction?.let { action ->
            when (action) {
                is TaskAction.DeleteTask -> {
                    val result = snackBarState.showSnackbar(
                        message = "Task deleted",
                        actionLabel = "Undo",
                        withDismissAction = true
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onUndoDelete(action.task)
                    }
                }

                is TaskAction.CompleteTask -> {
                    val result = snackBarState.showSnackbar(
                        message = "Task marked completed",
                        actionLabel = "Undo",
                        withDismissAction = true
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        onUndoComplete(action.taskId)
                    }
                }
            }
        }
    }
}