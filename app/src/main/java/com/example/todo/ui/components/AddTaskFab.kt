package com.example.todo.ui.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.example.test.add
import com.example.todo.ui.theme.AppTheme

@Composable
fun AddTaskFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .testTag("add_task_fab")
    ) {
        Icon(add, null)
    }
}

@Preview
@Composable
private fun AddTaskFabPrev() {
    AppTheme {
        AddTaskFab(
            onClick = {}
        )
    }
}