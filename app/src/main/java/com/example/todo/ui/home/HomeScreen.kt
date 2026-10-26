@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todo.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todo.models.Reminder
import com.example.todo.models.SortType
import com.example.todo.models.Task
import com.example.todo.models.TaskSection
import com.example.todo.models.toTaskSections
import com.example.todo.ui.components.AddTaskFab
import com.example.todo.ui.components.FilterType
import com.example.todo.ui.home.quickAdd.QuickAddSheet
import com.example.todo.ui.home.quickAdd.QuickAddViewModel
import com.example.todo.ui.home.quickAdd.TaskEditorState
import com.example.todo.ui.shared.TaskActionViewModel
import com.example.todo.ui.utils.FlatListShapes
import com.example.todo.ui.utils.SectionedListShapes
import com.example.todo.ui.utils.getHeaderDisplayColor
import com.example.todo.ui.utils.largeRadius
import kotlinx.serialization.Serializable
import sh.calvin.reorderable.rememberReorderableLazyListState

@Serializable
object Home

@Composable
fun HomeScreen(
    navigateToTaskScreen: (Long) -> Unit,
    navigateToRepeatFormScreen: (Reminder?) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    quickAddViewModel: QuickAddViewModel = hiltViewModel(),
    taskActionViewModel: TaskActionViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val taskEditorState by quickAddViewModel.taskEditorState.collectAsStateWithLifecycle()
    val taskActions by taskActionViewModel.taskActions.collectAsStateWithLifecycle(null)

    // a flag set when launch repeat form screen to  remember to show quick add sheet on return
    var shouldShowQuickAddSheet by rememberSaveable { mutableStateOf(false) }
    var showQuickAddSheet by rememberSaveable { mutableStateOf(false) }

    val snackBarState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()


    // Show form (sheet) when return from repeat Screen
    LaunchedEffect(Unit) {
        showQuickAddSheet = shouldShowQuickAddSheet
        shouldShowQuickAddSheet = false
    }

    TaskActionEffect(
        taskActions,
        snackBarState,
        onUndoDelete = homeViewModel::undoDelete,
        onUndoComplete = homeViewModel::toggleCompleteTask
    )

    Scaffold(
        topBar = { HomeAppBar(scrollBehavior = scrollBehavior) },
        floatingActionButton = {
            AddTaskFab(onClick = { showQuickAddSheet = true })
        },
        snackbarHost = {
            SnackbarHost(snackBarState)
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                LoadingScreen()
            }

            else -> {
                HomeContent(
                    // Tasks and filter
                    noTasks = uiState.noTasks,
                    selectedFilter = uiState.filter,
                    onSelectFilter = homeViewModel::updateFilter,
                    onDeleteAllCompletedTasks = homeViewModel::deleteAllCompleted,
                    onStar = homeViewModel::onStarTask,
                    // Sorting
                    taskSections = uiState.taskSections,
                    saveOrder = homeViewModel::saveOrder,
                    selectedSortType = uiState.sort,
                    onSelectSortType = homeViewModel::setSortType,
                    // Task actions
                    onTaskClicked = { navigateToTaskScreen(it.id) },
                    onTaskCompletionToggled = { task -> homeViewModel.toggleCompleteTask(task.id) },
                    // Quick add
                    taskEditorState = taskEditorState,
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
    taskSections: List<TaskSection>,
    noTasks: Boolean,
    selectedFilter: FilterType,
    onSelectFilter: (FilterType) -> Unit,
    onDeleteAllCompletedTasks: () -> Unit,
    onStar: (Task) -> Unit,

    // Sorting
    saveOrder: (List<Task>) -> Unit,
    selectedSortType: SortType,
    onSelectSortType: (SortType) -> Unit,

    // Task actions
    onTaskClicked: (Task) -> Unit,
    onTaskCompletionToggled: (Task) -> Unit,
    // Quick add
    showQuickAddSheet: Boolean,
    onDismissQuickAddSheet: () -> Unit,
    taskEditorState: TaskEditorState,
    navigateToRepeatForm: (Reminder?) -> Unit,
    onAddTask: () -> Unit,

    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    // Reorder
    val hapticFeedback = LocalHapticFeedback.current
    val reorderableList = remember(taskSections) { taskSections.firstOrNull()?.items?.toMutableStateList()?: mutableStateListOf() }
    val reorderableLazyListState = rememberReorderableLazyListState(
        lazyListState, scrollThresholdPadding = PaddingValues(20.dp)
    ) { from, to ->
        reorderableList.apply {
            add(to.index - 1, removeAt(from.index - 1))
        }
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    // Task sections
    val sections =
        if (selectedSortType == SortType.CUSTOM) reorderableList.toTaskSections(selectedSortType) else taskSections
    val listItemShapes = remember(selectedSortType) {
        if (selectedSortType in listOf(
                SortType.CUSTOM,
                SortType.TITLE
            )
        ) FlatListShapes else SectionedListShapes
    }

    val colorScheme = MaterialTheme.colorScheme
    var itemAnimationsEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(selectedSortType) {
        itemAnimationsEnabled = selectedSortType == SortType.CUSTOM
    }


    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .then(
                if (selectedSortType == SortType.CUSTOM) Modifier.background(
                    CardDefaults.cardColors().containerColor, shape = RoundedCornerShape(
                        largeRadius
                    )
                ) else Modifier
            )
    ) {
        if (taskSections.isEmpty()) {
            headerWithEmptyStateItem(
                noTasks = noTasks,
                selectedFilter = selectedFilter,
                onSelectFilter = onSelectFilter,
                selectedSortType = selectedSortType,
                onSelectSortType = onSelectSortType,
                onDeleteAllCompletedTasks = onDeleteAllCompletedTasks,
                shape = listItemShapes.emptyShape
            )
        } else {
            // 2. Header (Non-empty state)
            headerItem(
                selectedFilter = selectedFilter,
                onSelectFilter = onSelectFilter,
                selectedSortType = selectedSortType,
                onSelectSortType = onSelectSortType,
                onDeleteAllCompletedTasks = onDeleteAllCompletedTasks,
                shape = listItemShapes.headerShape
            )

            // 3. Sections
            sections.forEachIndexed { sectionIndex, section ->
                // Section header, if any
                section.header?.let { title ->
                    sectionHeader(
                        title = title,
                        selectedSortType = selectedSortType,
                        shape = listItemShapes.firstItemShape,
                        color = section.getHeaderDisplayColor(selectedSortType, colorScheme),
                        category = section.items.first().category
                    )
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
                        onStar = { onStar(task) },
                        sortType = selectedSortType,
                        saveOrder = {
                            saveOrder(reorderableList.toList())
                        },
                        enabled = selectedSortType == SortType.CUSTOM && selectedFilter == FilterType.ALL,
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
            taskEditorState = taskEditorState,
            onDismiss = onDismissQuickAddSheet,
            onAddTask = onAddTask,
            navigateToRepeatForm = navigateToRepeatForm,
            modifier = Modifier
        )
    }
}