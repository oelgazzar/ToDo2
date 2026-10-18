@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todo.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todo.data.sampleTodos
import com.example.todo.models.Task
import com.example.todo.ui.theme.AppTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class Home(val deletedTask: String? = null)

@Composable
fun HomeScreen(
    navigateToTaskScreen: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvents.collectAsStateWithLifecycle(null)

    var showForm by remember { mutableStateOf(false) }
    val snackBarState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiEvent) {
        // Show snackbar on delete + Undo?
        if (uiEvent != null) {
            coroutineScope.launch {
                val result = snackBarState.showSnackbar(
                    message = uiEvent!!,
                    actionLabel = "Undo",
                    withDismissAction = true
                )

                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.undoDelete()
                } else {
                    viewModel.deletePermanently()
                }
            }
        }
    }

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
                    onTaskCompleteStatusChanged = viewModel::toggleCompleteTask,
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

            else -> null
        }
    }
}

@Composable
fun HomeContent(
    tasks: List<Task>,
    onTaskClicked: (Task) -> Unit,
    onTaskCompleteStatusChanged: (Task, Boolean) -> Unit,
    showForm: Boolean,
    onDismissForm: () -> Unit,
    onAddTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    TaskList(
        tasks,
        onTaskClicked = onTaskClicked,
        onTaskCompleteStatusChanged = onTaskCompleteStatusChanged,
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
            onTaskCompleteStatusChanged = { _, _ -> },
            showForm = false,
            onDismissForm = {},
            onAddTask = {},
        )
    }
}

