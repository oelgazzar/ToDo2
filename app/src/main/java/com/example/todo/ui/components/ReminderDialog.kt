package com.example.todo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.test.repeat
import com.example.test.schedule
import com.example.todo.models.Reminder
import com.example.todo.ui.theme.AppTheme
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDialog(
    initialReminder: Reminder?,
    onConfirm: (Reminder) -> Unit,
    onDismiss: () -> Unit,
    showRepeatForm: (Reminder?) -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(initialReminder?.date ?: LocalDate.now())
    var timeReminder by remember { mutableStateOf(initialReminder?.time) }
    var showTimePicker by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
    ) {
        Card(
            modifier = modifier
                .padding(horizontal = 8.dp, vertical = 64.dp)
                .requiredWidth(360.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                DatePicker(
                    state = datePickerState,
                    title = null,
                    headline = null,
                    showModeToggle = false
                )
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .clickable(
                            onClick = { showTimePicker = true }
                        )
                        .padding(16.dp)
//                        .fillMaxWidth()
                ) {
                    Icon(
                        schedule, null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Select Time")
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                showRepeatForm(
                                    Reminder(
                                        datePickerState.getSelectedDate() ?: LocalDate.now(),
                                        timeReminder
                                    )
                                )
                            }
                        )
                        .padding(16.dp)
//                        .fillMaxWidth()
                ) {
                    Icon(
                        repeat, null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Repeat")
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.End)
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onConfirm(
                                Reminder(
                                    datePickerState.getSelectedDate() ?: LocalDate.now(),
                                    timeReminder
                                )
                            )
                        }
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            initialTime = LocalTime.of(
                initialReminder?.time?.hour ?: LocalTime.now().hour,
                initialReminder?.time?.minute ?: LocalTime.now().minute
            ),
            onConfirm = {
                timeReminder = LocalTime.of(it.hour, it.minute)
                showTimePicker = false
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }
}

@Preview
@Composable
private fun ReminderDialogPrev() {
    AppTheme {
        ReminderDialog(
            initialReminder = Reminder(LocalDate.now()),
            onConfirm = {},
            onDismiss = {},
            showRepeatForm = {}
        )
    }
}