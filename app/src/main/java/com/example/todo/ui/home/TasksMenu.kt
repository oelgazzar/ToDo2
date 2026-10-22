package com.example.todo.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.test.more_vert
import com.example.test.swap_vert
import com.example.todo.models.SortType

@Composable
fun TasksMenu(
    expanded: Boolean,
    onExpand: () -> Unit,
    onDismiss: () -> Unit,
    onDeleteCompletedTasks: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        IconButton(
            onClick = onExpand
        ) {
            Icon(more_vert, null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss
        ) {
            DropdownMenuItem(
                text = {
                    Text("Delete completed tasks")
                },
                onClick = {
                    onDeleteCompletedTasks()
                    onDismiss()
                }
            )
        }
    }
}