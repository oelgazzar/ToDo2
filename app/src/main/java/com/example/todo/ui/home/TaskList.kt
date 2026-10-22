package com.example.todo.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.models.Task
import com.example.todo.ui.components.FilterRow
import com.example.todo.ui.components.FilterType
import com.example.todo.ui.theme.AppTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun TaskList(
    tasks: List<Task>,
    totalTaskCount: Int,
    selectedFilter: FilterType,
    onFilterChanged: (FilterType) -> Unit,
    saveOrder: (List<Task>) -> Unit,

    onTaskClicked: (Task) -> Unit,
    onTaskCompletionToggled: (Task) -> Unit,

    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    var showSortingMenu by remember { mutableStateOf(false) }
    var showTasksMenu by remember { mutableStateOf(false) }
    val reorderableList = remember(tasks) { tasks.toMutableStateList() }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(
        lazyListState,
        scrollThresholdPadding = PaddingValues(20.dp)
    ) { from, to ->
        reorderableList.apply {
            add(to.index, removeAt(from.index))
        }
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
        ) {
            /* Header */
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "My Tasks",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(bottom = 8.dp)
                        .weight(1f)
                )

                SortingMenu(
                    expanded = showSortingMenu,
                    onExpand = { showSortingMenu = true },
                    onDismiss = { showSortingMenu = false },
                    onSortChanged = { sortType ->

                    },
                    modifier = Modifier.padding(end = 8.dp)
                )

                TasksMenu(
                    expanded = showTasksMenu,
                    onExpand = { showTasksMenu = true },
                    onDismiss = { showTasksMenu = false },
                    onDeleteCompletedTasks = {}
                )
            }

            /* Filter options */
            if (tasks.isNotEmpty() || selectedFilter != FilterType.ALL) {
                FilterRow(
                    selectedFilter = selectedFilter,
                    setSelectedFilterType = onFilterChanged,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )
            }

            /* Empty state */
            if (tasks.isEmpty()) {
                EmptyScreen(
                    totalTaskCount = totalTaskCount,
                    selectedFilter = selectedFilter,
                    modifier = Modifier.padding(16.dp)
                )

                /* Actual lists */
            } else {
                LazyColumn(
                    state = lazyListState,
                ) {
                    items(reorderableList, key = { it.id }) { taskItem ->
                        ReorderableItem(
                            reorderableLazyListState,
                            key = taskItem.id
                        ) { isDragging ->
                            val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                            val scale by animateFloatAsState(if (isDragging) 1.05f else 1f)

                            TaskItem(
                                task = taskItem,
                                onTaskClicked = { onTaskClicked(taskItem) },
                                onToggleCompleted = {
                                    onTaskCompletionToggled(
                                        taskItem,
                                    )
                                },
                                modifier = Modifier
                                    .longPressDraggableHandle(
                                        onDragStarted = {
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                                        },
                                        onDragStopped = {
                                            saveOrder(reorderableList.toList())
                                            hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                        }
                                    )
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    .shadow(elevation)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskListPrev() {
    AppTheme {
        TaskList(
            tasks = listOf(
                Task(
                    title = "Task 1",
                    position = -1,
                ),
                Task(
                    id = 1,
                    title = "Task 2",
                    position = -1,
                ),
                Task(
                    id = 3,
                    title = "Task 3",
                    position = -1,
                ),
            ),
            totalTaskCount = 3,
            onTaskClicked = {},
            onTaskCompletionToggled = {},
            selectedFilter = FilterType.ALL,
            onFilterChanged = {},
            saveOrder = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun EmptyTaskListPrev() {
    AppTheme {
        TaskList(
            tasks = listOf(

            ),
            totalTaskCount = 0,
            onTaskClicked = {},
            onTaskCompletionToggled = {},
            selectedFilter = FilterType.ALL,
            onFilterChanged = {},
            saveOrder = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}