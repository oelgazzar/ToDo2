package com.example.todo.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.models.Task
import com.example.todo.ui.theme.AppTheme

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClicked: (Task) -> Unit,
    onTaskCompleteStatusChanged: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        LazyColumn(
//            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier
                .padding(16.dp),
        ) {
            /* Header */
            item {
                Text(
                    "My Tasks",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (tasks.isEmpty()) {
                item {
                    EmptyScreen(
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onTaskClicked = { onTaskClicked (task)},
                        onTaskCompleteStatusChanged = { isCompleted ->
                            onTaskCompleteStatusChanged(
                                task,
                                isCompleted
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
            onTaskClicked = {},
            onTaskCompleteStatusChanged = { _, _ -> },
            modifier = Modifier.padding(16.dp)
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
            onTaskClicked = {},
            onTaskCompleteStatusChanged = { _, _ -> },
            modifier = Modifier.padding(16.dp)
        )
    }
}