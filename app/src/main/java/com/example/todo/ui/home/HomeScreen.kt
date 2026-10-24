@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todo.ui.home

import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todo.data.sampleTodos
import com.example.todo.models.Reminder
import com.example.todo.models.SortType
import com.example.todo.models.Task
import com.example.todo.models.TaskSection
import com.example.todo.models.toTaskSections
import com.example.todo.ui.components.AddTaskFab
import com.example.todo.ui.components.FilterRow
import com.example.todo.ui.components.FilterType
import com.example.todo.ui.home.quickAdd.QuickAddSheet
import com.example.todo.ui.home.quickAdd.QuickAddViewModel
import com.example.todo.ui.home.quickAdd.TaskDraftState
import com.example.todo.ui.shared.TaskActionViewModel
import com.example.todo.ui.theme.AppTheme
import com.example.todo.ui.utils.FlatListShapes
import com.example.todo.ui.utils.SectionedListShapes
import com.example.todo.ui.utils.getHeaderDisplayColor
import kotlinx.serialization.Serializable
import sh.calvin.reorderable.rememberReorderableLazyListState

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
                    // Sorting
                    taskSections = uiState.taskSections,
                    saveOrder = homeViewModel::saveOrder,
                    sortType = uiState.sort,
                    setSortType = homeViewModel::setSortType,
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

    // Sorting
    taskSections: List<TaskSection> = listOf(),
    saveOrder: (List<Task>) -> Unit,
    sortType: SortType,
    setSortType: (SortType) -> Unit,

    // Task actions
    onTaskClicked: (Task) -> Unit,
    onTaskCompletionToggled: (Task) -> Unit,
    // Quick add
    showQuickAddSheet: Boolean,
    onDismissQuickAddSheet: () -> Unit,
    taskDraftState: TaskDraftState,
    navigateToRepeatForm: (Reminder?) -> Unit,
    onAddTask: () -> Unit,

    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    // Reorder
    val hapticFeedback = LocalHapticFeedback.current
    val reorderableList = remember(tasks) { tasks.toMutableStateList() }
    val reorderableLazyListState = rememberReorderableLazyListState(
        lazyListState, scrollThresholdPadding = PaddingValues(20.dp)
    ) { from, to ->
        reorderableList.apply {
            add(to.index - 1, removeAt(from.index - 1))
            println("from: ${from.index} to: ${to.index}")
        }
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    // Task sections
    val sections =
        if (sortType == SortType.CUSTOM) reorderableList.toTaskSections(sortType) else taskSections
    println(sections)
    val listItemShapes = remember(sortType) {
        if (sortType in listOf(
                SortType.CUSTOM,
                SortType.TITLE
            )
        ) FlatListShapes else SectionedListShapes
    }

    val colorScheme = MaterialTheme.colorScheme
    var itemAnimationsEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(sortType) {
        itemAnimationsEnabled = sortType == SortType.CUSTOM
    }


    LazyColumn(
        state = lazyListState,
        modifier = modifier
    ) {
        if (tasks.isEmpty()) {
            // 1. Header (Empty state)
            item(key = "empty_card") {
                Card(shape = listItemShapes.emptyShape) {
                    TaskListHeader(sortType, setSortType)
                    FilterRow(
                        selectedFilter = selectedFilter,
                        setSelectedFilterType = onFilterChanged,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp)
                    )
                    EmptyScreen(
                        totalTaskCount = totalTaskCount,
                        selectedFilter = selectedFilter,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            // 2. Header (Non-empty state)
            item(key = "header") {
                Card(shape = listItemShapes.headerShape) {
                    TaskListHeader(sortType, setSortType)
                    FilterRow(
                        selectedFilter = selectedFilter,
                        setSelectedFilterType = onFilterChanged,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp)
                    )
                }
            }

            // 3. Sections
            sections.forEachIndexed { sectionIndex, section ->
                // Section header, if any
                section.header?.let { title ->
                    item(key = "title_${title}") {
                        Card(
                            shape = listItemShapes.firstItemShape,
                            modifier = Modifier.padding(top = 1.dp) // gap above section
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                color = section.getHeaderDisplayColor(sortType, colorScheme),
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                // Section Item
                itemsIndexed(section.items, key = { _, t -> t.id }) { i, task ->
                    val isFirst = i == 0 && section.header == null
                    val isLast = i == section.items.lastIndex
                    ReorderableTaskItem(
                        reorderableLazyListState = reorderableLazyListState,
                        taskItem = task,
                        onTaskClicked = { onTaskClicked(task) },
                        onTaskCompletionToggled = {
                            onTaskCompletionToggled(
                                task,
                            )
                        },
                        saveOrder = {
                            saveOrder(reorderableList.toList())
                        },
                        enabled = sortType == SortType.CUSTOM && selectedFilter == FilterType.ALL,
                        shape = when {
                            isLast && sectionIndex == sections.lastIndex -> listItemShapes.bottomItemShape
                            isLast -> listItemShapes.lastItemShape
                            isFirst -> listItemShapes.firstItemShape
                            else -> RectangleShape
                        },
                        animateItem = itemAnimationsEnabled
                    )
                }
            }
        }
    }

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
private fun HomeContentWithSectionsPrev() {
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
            sortType = SortType.DEADLINE,
            setSortType = {},
            saveOrder = {},
            onFilterChanged = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeContentFlatPrev() {
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
            sortType = SortType.CUSTOM,
            setSortType = {},
            saveOrder = {},
            onFilterChanged = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeContentEmptyPrev() {
    AppTheme {
        HomeContent(
            tasks = listOf(),
            totalTaskCount = 0,
            onTaskClicked = {},
            onTaskCompletionToggled = {},
            showQuickAddSheet = false,
            onDismissQuickAddSheet = {},
            taskDraftState = TaskDraftState(),
            navigateToRepeatForm = {},
            onAddTask = {},
            selectedFilter = FilterType.ALL,
            sortType = SortType.DEADLINE,
            setSortType = {},
            saveOrder = {},
            onFilterChanged = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeContentWithSectionsSinglePrev() {
    AppTheme {
        HomeContent(
            tasks = listOf(
                sampleTodos.first()
            ),
            totalTaskCount = 1,
            onTaskClicked = {},
            onTaskCompletionToggled = {},
            showQuickAddSheet = false,
            onDismissQuickAddSheet = {},
            taskDraftState = TaskDraftState(),
            navigateToRepeatForm = {},
            onAddTask = {},
            selectedFilter = FilterType.ALL,
            sortType = SortType.DEADLINE,
            setSortType = {},
            saveOrder = {},
            onFilterChanged = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeContentFlaySinglePrev() {
    AppTheme {
        HomeContent(
            tasks = listOf(
                sampleTodos.first()
            ),
            totalTaskCount = 1,
            onTaskClicked = {},
            onTaskCompletionToggled = {},
            showQuickAddSheet = false,
            onDismissQuickAddSheet = {},
            taskDraftState = TaskDraftState(),
            navigateToRepeatForm = {},
            onAddTask = {},
            selectedFilter = FilterType.ALL,
            sortType = SortType.CUSTOM,
            setSortType = {},
            saveOrder = {},
            onFilterChanged = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
