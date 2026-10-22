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
import com.example.todo.ui.home.quickAdd.QuickAddSheet
import com.example.todo.ui.home.quickAdd.QuickAddViewModel
import com.example.todo.ui.home.quickAdd.TaskDraftState
import com.example.todo.ui.shared.TaskActionViewModel
import com.example.todo.ui.theme.AppTheme
import kotlinx.serialization.Serializable

@Serializable
object Home

@Composable
fun HomeScreen(
    navigateToTaskScreen: (Long) -> Unit,
    navigateToRepeatFormScreen: (Reminder?) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    quickAddViewModel: QuickAddViewModel = hiltViewModel()
) {
    // For published edit actions e.g. delete and complete
    val taskActionViewModel: TaskActionViewModel =
        hiltViewModel(LocalActivity.current as ViewModelStoreOwner)

    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val taskDraftState by quickAddViewModel.taskDraftState.collectAsStateWithLifecycle()
    val taskAction by taskActionViewModel.taskAction.collectAsStateWithLifecycle(null)

    var shouldShowQuickAddSheet by rememberSaveable { mutableStateOf(false) }
    var showQuickAddSheet by rememberSaveable { mutableStateOf(false) }
    val snackBarState = remember { SnackbarHostState() }


    // Show form (sheet) when return from repeat Screen
    LaunchedEffect(Unit) {
        showQuickAddSheet = shouldShowQuickAddSheet
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
            AddTaskFab(onClick = { showQuickAddSheet = true })
        },
        snackbarHost = {
            SnackbarHost(snackBarState)
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                LoadingScreen()
            }

            else -> {
                HomeContent(
                    // Tasks and filter
                    tasks = uiState.tasks,
                    totalTaskCount = uiState.taskCount,
                    selectedFilter = uiState.filter,
                    onFilterChanged = homeViewModel::updateFilter,
                    // Task actions
                    onTaskClicked = { navigateToTaskScreen(it.id) },
                    onTaskCompletionToggled = { task -> homeViewModel.toggleCompleteTask(task.id) },
                    // Quick add
                    taskDraftState = taskDraftState,
                    onAddTask = quickAddViewModel::addTask,
                    showQuickAddSheet = showQuickAddSheet,
                    onDismissQuickAddSheet = {
                        showQuickAddSheet = false
                        quickAddViewModel.reset()
                    },
                    navigateToRepeatForm = {
                        navigateToRepeatFormScreen(it)
                        shouldShowQuickAddSheet = true
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun HomeContent(
    // Tasks and filter
    tasks: List<Task>,
    totalTaskCount: Int,
    selectedFilter: FilterType,
    onFilterChanged: (FilterType) -> Unit,
    // Task actions
    onTaskClicked: (Task) -> Unit,
    onTaskCompletionToggled: (Task) -> Unit,
    // Quick add
    showQuickAddSheet: Boolean,
    onDismissQuickAddSheet: () -> Unit,
    taskDraftState: TaskDraftState,
    navigateToRepeatForm: (Reminder?) -> Unit,
    onAddTask: () -> Unit,

    modifier: Modifier = Modifier
) {
    TaskList(
        tasks,
        totalTaskCount = totalTaskCount,
        selectedFilter = selectedFilter,
        onFilterChanged = onFilterChanged,

        onTaskClicked = onTaskClicked,
        onTaskCompletionToggled = onTaskCompletionToggled,
        modifier = modifier
    )

    if (showQuickAddSheet) {
        QuickAddSheet(
            taskDraftState = taskDraftState,
            onDismiss = onDismissQuickAddSheet,
            onAddTask = onAddTask,
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
            showQuickAddSheet = false,
            onDismissQuickAddSheet = {},
            taskDraftState = TaskDraftState(),
            navigateToRepeatForm = {},
            onAddTask = {},
            selectedFilter = FilterType.ALL,
            onFilterChanged = {}
        )
    }
}

