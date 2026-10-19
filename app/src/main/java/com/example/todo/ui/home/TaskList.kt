package com.example.todo.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.swap_vert
import com.example.todo.models.Task
import com.example.todo.ui.components.FilterRow
import com.example.todo.ui.components.FilterType
import com.example.todo.ui.theme.AppTheme

@Composable
fun TaskList(
    tasks: List<Task>,
    totalTaskCount: Int,
    onTaskClicked: (Task) -> Unit,
    onTaskCompletionToggled: (Task) -> Unit,
    selectedFilter: FilterType,
    onFilterChanged: (FilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
        ) {
            /* Header */
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "My Tasks",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                            .weight(1f)
                    )

                    Box {
                        IconButton(
                            onClick = { showMenu = true }
                        ) {
                            Icon(swap_vert, null)
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false}
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text("My order")
                                },
                                onClick = {
                                    showMenu = false
                                }
                            )
                        }

                    }


                }
            }

            if (tasks.isNotEmpty() || selectedFilter != FilterType.ALL) {
                item {
                    FilterRow(
                        selectedFilter = selectedFilter,
                        setSelectedFilterType = onFilterChanged,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            if (tasks.isEmpty()) {
                item {
                    EmptyScreen(
                        totalTaskCount = totalTaskCount,
                        selectedFilter = selectedFilter,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onTaskClicked = { onTaskClicked (task)},
                        onToggleCompleted = {
                            onTaskCompletionToggled(
                                task,
                                )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
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
                    id = 0,
                    title = "Task 1",
                    category = null,
                ),
                Task(
                    id = 1,
                    title = "Task 2",
                    category = null,
                ),
                Task(
                    id = 3,
                    title = "Task 3",
                    category = null,
                ),
            ),
            totalTaskCount = 3,
            onTaskClicked = {},
            onTaskCompletionToggled = {},
            selectedFilter = FilterType.ALL,
            onFilterChanged = {},
            modifier = Modifier.padding(16.dp).fillMaxWidth()
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
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        )
    }
}