package com.example.todo.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.models.Schedule
import com.example.todo.models.SortType
import com.example.todo.models.Task
import com.example.todo.models.toTaskSections
import com.example.todo.ui.components.FilterType
import com.example.todo.ui.theme.AppTheme
import sh.calvin.reorderable.rememberReorderableLazyListState
import java.time.LocalDate

@Composable
fun TaskList(
    tasks: List<Task>,
    totalTaskCount: Int,
    selectedFilter: FilterType,
    onFilterChanged: (FilterType) -> Unit,

    saveOrder: (List<Task>) -> Unit,
    sortType: SortType,
    setSortType: (SortType) -> Unit,

    onTaskClicked: (Task) -> Unit,
    onTaskCompletionToggled: (Task) -> Unit,

    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    val reorderableList = remember(tasks) { tasks.toMutableStateList() }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(
        lazyListState, scrollThresholdPadding = PaddingValues(20.dp)
    ) { from, to ->
        reorderableList.apply {
            add(to.index, removeAt(from.index))
        }
        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    val taskSections = reorderableList.toTaskSections(sortType)

    Column(
        modifier = modifier
    ) {
        LazyColumn(
            state = lazyListState,
        ) {
            taskSections.forEachIndexed { index, section ->
                if (section.header != null) {
                    item {
                        Surface(color = CardDefaults.cardColors().containerColor) {
                            Text(
                                section.header,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                items(section.items, key = { it.id }) { taskItem ->
                    if (sortType == SortType.CUSTOM) {
                        ReorderableTaskItem(
                            reorderableLazyListState = reorderableLazyListState,
                            taskItem = taskItem,
                            onTaskClicked = { onTaskClicked(taskItem) },
                            onTaskCompletionToggled = {
                                onTaskCompletionToggled(
                                    taskItem,
                                )
                            },
                            sortType = sortType,
                            saveOrder = {
                                saveOrder(reorderableList.toList())
                            },
                            shape = RectangleShape
                        )
                    } else {
                        TaskItem(
                            task = taskItem,
                            onTaskClicked = { onTaskClicked(taskItem) },
                            onToggleCompleted = {
                                onTaskCompletionToggled(
                                    taskItem,
                                )
                            },
                            sortType = sortType,
                            modifier = Modifier
                        )
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
                    title = "Task 1", position = -1, schedule = Schedule(
                        dueDate = LocalDate.now()
                    )
                ),
                Task(
                    id = 1, title = "Task 2", position = -1, schedule = Schedule(
                        dueDate = LocalDate.now()
                    )
                ),
                Task(
                    id = 3, title = "Task 3", position = -1, schedule = Schedule(
                        dueDate = LocalDate.now().plusDays(1)
                    )
                ),
            ),
            totalTaskCount = 3,
            onTaskClicked = {},
            onTaskCompletionToggled = {},
            selectedFilter = FilterType.ALL,
            onFilterChanged = {},
            saveOrder = {},
            sortType = SortType.DEADLINE,
            setSortType = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun CustomTaskListPrev() {
    AppTheme {
        TaskList(
            tasks = listOf(
                Task(
                    title = "Task 1", position = -1, schedule = Schedule(
                    )
                ),
                Task(
                    id = 1, title = "Task 2", position = -1, schedule = Schedule(
                    )
                ),
                Task(
                    id = 3, title = "Task 3", position = -1, schedule = Schedule(
                    )
                ),
            ),
            totalTaskCount = 3,
            onTaskClicked = {},
            onTaskCompletionToggled = {},
            selectedFilter = FilterType.ALL,
            onFilterChanged = {},
            saveOrder = {},
            sortType = SortType.CUSTOM,
            setSortType = {},
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
            sortType = SortType.CUSTOM,
            setSortType = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}