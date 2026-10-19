package com.example.todo.ui.task

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test.list_2
import com.example.todo.models.Task
import com.example.todo.ui.components.TransparentTextField
import com.example.todo.ui.shared.TaskActionViewModel
import com.example.todo.ui.theme.AppTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class TaskDetails(val taskId: Long)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    navigateBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    val currentTask by viewModel.currentTask.collectAsStateWithLifecycle()
    val taskActionViewModel: TaskActionViewModel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)

    BackHandler {
        coroutineScope.launch {
            viewModel.saveTask()
            navigateBack()
        }
    }

    Scaffold(
        topBar = {
            TaskAppBar(
                navigateBack = {
                    coroutineScope.launch {
                        viewModel.saveTask()
                        navigateBack()
                    }
                },
                onDelete = {
                    showConfirmDeleteDialog = true
                }
            )
        }
    ) { innerPadding ->
        TaskScreenContent(
            titleFieldState = viewModel.titleFieldState,
            detailsFieldState = viewModel.detailsFieldState,
            currentTask = currentTask,
            toggleCompleted = {
                coroutineScope.launch {
                    val taskCompleted = viewModel.toggleCompleted()
                    if (taskCompleted) {
                        taskActionViewModel.publishCompletedTask(viewModel.taskId)
                        navigateBack()
                    }
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        )
    }

    if (showConfirmDeleteDialog) {
        ConfirmDeleteDialog(
            onDismiss = {showConfirmDeleteDialog = false },
            onConfirm = {
                coroutineScope.launch {
                    showConfirmDeleteDialog = false
                    val task = viewModel.deleteTask()
                    task?.let {
                        taskActionViewModel.publishDeletedTask(task)
                    }
                    navigateBack()
                }
            }
        )
    }
}

@Composable
fun TaskScreenContent(
    titleFieldState: TextFieldState,
    detailsFieldState: TextFieldState,
    currentTask: Task?,
    toggleCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        TransparentTextField(
            state = titleFieldState,
            placeholderText = "Enter title",
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                list_2,
                null,
            )
            TransparentTextField(
                state = detailsFieldState,
                placeholderText = "Add details",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alignBy(FirstBaseline)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = toggleCompleted,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (currentTask?.completed == true) "Mark uncompleted" else "Mark completed")
        }
    }
}

@Preview
@Composable
private fun TaskScreenContentPrev() {
    AppTheme {
        TaskScreenContent(
            titleFieldState = rememberTextFieldState(),
            detailsFieldState = rememberTextFieldState(),
            currentTask = Task(
                id = 0,
                title = "Task 1",
                category = null,
            ),
            toggleCompleted = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}