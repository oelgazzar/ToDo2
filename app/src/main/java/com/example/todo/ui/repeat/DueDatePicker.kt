package com.example.todo.ui.repeat

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import java.time.LocalDate

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DueDatePicker(
    initialSelectedDate: LocalDate?,
    onDismiss: () -> Unit,
    onDueDateChanged: (LocalDate?) -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDate)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDueDateChanged(datePickerState.getSelectedDate())
                onDismiss()
            }) { Text("Ok") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    ) {
        DatePicker(datePickerState)
    }
}