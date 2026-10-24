package com.example.todo.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.models.SortType

@Composable
fun TaskListHeader(
    sortType: SortType,
    setSortType: (SortType) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortingMenu by remember { mutableStateOf(false) }
    var showTasksMenu by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
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
            selectedSortType = sortType,
            onSortChanged = setSortType,
            modifier = Modifier.padding(end = 8.dp)
        )

        TasksMenu(
            expanded = showTasksMenu,
            onExpand = { showTasksMenu = true },
            onDismiss = { showTasksMenu = false },
            onDeleteCompletedTasks = {})
    }
}