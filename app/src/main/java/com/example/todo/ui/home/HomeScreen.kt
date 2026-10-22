@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todo.ui.home

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todo.data.sampleTodos
import com.example.todo.models.Reminder
import com.example.todo.models.Task
import com.example.todo.ui.components.AddTaskFab
import com.example.todo.ui.components.FilterType
import com.example.todo.ui.shared.TaskActionViewModel
import com.example.todo.ui.theme.AppTheme
import kotlinx.serialization.Serializable

@Serializable
object Home

@Composable
fun HomeScreen(
    navigateToTaskScreen: (Long) -> Unit,
    navigateToRepeatForm: (Reminder?) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    quickAddViewModel: QuickAddViewModel = hiltViewModel()
) {
    val taskActionViewModel: TaskActionViewModel =
        hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val taskAction by taskActionViewModel.taskAction.collectAsStateWithLifecycle(null)

    var showForm by rememberSaveable { mutableStateOf(false) }
    val snackBarState = remember { SnackbarHostState() }
    val taskDraftState by quickAddViewModel.taskDraftState.collectAsStateWithLifecycle()
    var shouldShowForm by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showForm = shouldShowForm
    }


    TaskActionEffect(
        taskAction,
        snackBarState,
        onUndoDelete = homeViewModel::undoDelete,
        onUndoComplete = homeViewModel::toggleCompleteTask
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
        when {
            uiState.isLoading -> {
                LoadingScreen(modifier)
            }

            else -> {
                HomeContent(
                    tasks = uiState.tasks,
                    totalTaskCount = uiState.taskCount,
                    onTaskClicked = { navigateToTaskScreen(it.id) },
                    onTaskCompletionToggled = { task -> homeViewModel.toggleCompleteTask(task.id) },
                    showForm = showForm,
                    onDismissForm = {
                        showForm = false
                        quickAddViewModel.reset()
                    },
                    taskDraftState = taskDraftState,
                    navigateToRepeatForm = {
                        navigateToRepeatForm(it)
                        shouldShowForm = true
                    },
                    onAddTask = quickAddViewModel::addTask,
                    selectedFilter = uiState.filter,
                    onFilterChanged = homeViewModel::updateFilter,
                    modifier = modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    tasks: List<Task>,
    totalTaskCount: Int,
    onTaskClicked: (Task) -> Unit,
    onTaskCompletionToggled: (Task) -> Unit,
    showForm: Boolean,
    onDismissForm: () -> Unit,
    taskDraftState: TaskDraftState,
    navigateToRepeatForm: (Reminder?) -> Unit,
    onAddTask: () -> Unit,
    selectedFilter: FilterType,
    onFilterChanged: (FilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    TaskList(
        tasks,
        totalTaskCount = totalTaskCount,
        onTaskClicked = onTaskClicked,
        onTaskCompletionToggled = onTaskCompletionToggled,
        selectedFilter = selectedFilter,
        onFilterChanged = onFilterChanged,
        modifier = modifier
    )

    if (showForm) {
        TaskFormSheet(
            onDismiss = onDismissForm,
            onAddTask = onAddTask,
            taskDraftState = taskDraftState,
            navigateToRepeatForm = navigateToRepeatForm,
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
            totalTaskCount = sampleTodos.size,
            onTaskClicked = {},
            onTaskCompletionToggled = {},
            showForm = false,
            onDismissForm = {},
            taskDraftState = TaskDraftState(),
            navigateToRepeatForm = {},
            onAddTask = {},
            selectedFilter = FilterType.ALL,
            onFilterChanged = {}
        )
    }
}

