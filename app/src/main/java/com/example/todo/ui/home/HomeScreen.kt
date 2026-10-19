@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todo.ui.home

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todo.data.sampleTodos
import com.example.todo.models.Task
import com.example.todo.ui.shared.TaskActionViewModel
import com.example.todo.ui.theme.AppTheme
import kotlinx.serialization.Serializable

@Serializable
object Home

@Composable
fun HomeScreen(
    navigateToTaskScreen: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val taskActionViewModel: TaskActionViewModel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val taskAction by taskActionViewModel.taskAction.collectAsStateWithLifecycle(null)

    var showForm by remember { mutableStateOf(false) }
    val snackBarState = remember { SnackbarHostState() }

    TaskActionEffect(
        taskAction,
        snackBarState,
        onUndoDelete = viewModel::undoDelete,
        onUndoComplete = viewModel::toggleCompleteTask
    )

    Scaffold(
        topBar = { HomeAppBar() },
        floatingActionButton = {
            AddTaskFab(onClick = { showForm = true })
        },
        snackbarHost = {
            SnackbarHost(snackBarState)
        }
    ) { innerPadding ->
        when (uiState) {
            is HomeUiState.Success -> {
                HomeContent(
                    tasks = (uiState as HomeUiState.Success).tasks,
                    onTaskClicked = { navigateToTaskScreen(it.id) },
                    onTaskCompletionToggled = { task -> viewModel.toggleCompleteTask(task.id) },
                    showForm = showForm,
                    onDismissForm = { showForm = false },
                    onAddTask = viewModel::addTask,
                    modifier = modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
            }

            is HomeUiState.Loading -> {
                LoadingScreen(modifier)
            }

            else -> Unit
        }
    }
}

@Composable
fun HomeContent(
    tasks: List<Task>,
    onTaskClicked: (Task) -> Unit,
    onTaskCompletionToggled: (Task) -> Unit,
    showForm: Boolean,
    onDismissForm: () -> Unit,
    onAddTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    TaskList(
        tasks,
        onTaskClicked = onTaskClicked,
        onTaskCompletionToggled = onTaskCompletionToggled,
        modifier = modifier
    )

    if (showForm) {
        TaskFormSheet(
            onDismiss = onDismissForm,
            onAddTask = onAddTask,
            modifier = Modifier
        )

    }

}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeContentPrev() {
    AppTheme {
        HomeContent(
            tasks = sampleTodos,
            onTaskClicked = {},
            onTaskCompletionToggled = {},
            showForm = false,
            onDismissForm = {},
            onAddTask = {},
        )
    }
}

