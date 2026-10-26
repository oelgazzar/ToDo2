package com.example.todo.ui.utils

import com.example.todo.R
import com.example.todo.ui.components.FilterType
import com.example.todo.ui.home.EmptyStateUI

fun FilterType.toEmptyStateUI(noTasks: Boolean) = if (noTasks) {
    EmptyStateUI(
        image = R.drawable.no_tasks,
        title = "No tasks available",
        description = "Tap + to add new task"
    )
} else {
    when (this) {
        FilterType.ALL -> EmptyStateUI(
            image = R.drawable.no_tasks,
            title = "No tasks available",
            description = "Tap + to add new task"
        )

        FilterType.COMPLETED -> EmptyStateUI(
            image = R.drawable.no_completed,
            title = "No completed tasks.",
            description = "You can do it"
        )

        FilterType.ACTIVE -> EmptyStateUI(
            image = R.drawable.all_completed,
            title = "All tasks completed",
            description = "Nice work!"
        )
    }
}