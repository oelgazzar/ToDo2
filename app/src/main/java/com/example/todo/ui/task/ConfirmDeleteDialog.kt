package com.example.todo.ui.task

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        text = {
            Text("Are you sure you want to delete this task?")
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("YES")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("NO")
            }
        }
    )
}