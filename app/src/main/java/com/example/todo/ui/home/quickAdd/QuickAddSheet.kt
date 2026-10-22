@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todo.ui.home.quickAdd

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.menu_open
import com.example.test.repeat
import com.example.test.schedule
import com.example.test.star
import com.example.todo.models.Reminder
import com.example.todo.models.Repeat
import com.example.todo.ui.components.FieldChip
import com.example.todo.ui.components.ReminderDialog
import com.example.todo.ui.components.TransparentTextField
import com.example.todo.ui.repeat.RepeatType
import com.example.todo.ui.theme.AppTheme
import com.example.todo.ui.utils.toDisplayText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun QuickAddSheet(
    taskDraftState: TaskDraftState,
    onAddTask: () -> Unit,
    onDismiss: () -> Unit,
    navigateToRepeatForm: (Reminder?) -> Unit,
    modifier: Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var showDetailsField by remember { mutableStateOf(false) }

    val titleFocusRequester = remember { FocusRequester() }
    val detailsFocusRequester = remember { FocusRequester() }
    var showReminderDialog by remember { mutableStateOf(false) }
    var showDiscardTaskConfirmDialog by remember { mutableStateOf(false) }


    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    // Set focus on text fields
    LaunchedEffect(sheetState.currentValue, showDetailsField) {
        if (sheetState.currentValue == SheetValue.Expanded) {
            titleFocusRequester.requestFocus()
        }

        if (showDetailsField) {
            delay(50.milliseconds)
            detailsFocusRequester.requestFocus()
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onDismiss()
                }
        },
        dragHandle = null,
        sheetState = sheetState,
        modifier = modifier
    ) {
        QuickAddSheetContent(
            taskDraftState = taskDraftState,
            titleFocusRequester = titleFocusRequester,
            detailsFocusRequester = detailsFocusRequester,
            onDismiss = {
                coroutineScope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onDismiss()
                }
            },
            onAddTask = onAddTask,
            navigateToRepeatForm = navigateToRepeatForm,
            onShowReminderDialog = { showReminderDialog = true }
        )
    }

    if (showReminderDialog) {
        ReminderDialog(
            initialReminder = taskDraftState.reminder,
            onDismiss = { showReminderDialog = false },
            onConfirm = {
                taskDraftState.updateReminder(it)
                showReminderDialog = false
            },
            showRepeatForm = {
                coroutineScope.launch {
                    sheetState.hide()
                    navigateToRepeatForm(it)
                    showReminderDialog = false
                }
            }
        )
    }

    if (showDiscardTaskConfirmDialog) {
        AlertDialog(
            text = {
                Text("Are you sure you want to discard this task?")
            },
            onDismissRequest = { showDiscardTaskConfirmDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDiscardTaskConfirmDialog = false
                    coroutineScope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        onDismiss()
                    }
                }) {
                    Text("YES")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDiscardTaskConfirmDialog = false
                }) {
                    Text("NO")
                }
            }
        )
    }
}

@Composable
fun QuickAddSheetContent(
    taskDraftState: TaskDraftState,
    titleFocusRequester: FocusRequester,
    detailsFocusRequester: FocusRequester,
    onAddTask: () -> Unit,
    onDismiss: () -> Unit,
    navigateToRepeatForm: (Reminder?) -> Unit,
    onShowReminderDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDetailsField by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding()
            .imePadding()
    ) {
        // --------------------- title -------------------------- //
        TransparentTextField(
            state = taskDraftState.title,
            placeholderText = "New Task",
            focusRequester = titleFocusRequester,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // --------------------- Details -------------------------- //
        if (showDetailsField) {
            TransparentTextField(
                state = taskDraftState.details,
                placeholderText = "Add details",
                style = MaterialTheme.typography.bodyMedium,
                focusRequester = detailsFocusRequester,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // --------------------- Reminder -------------------------- //
        if (taskDraftState.reminder != null) {
            FieldChip(
                label = (taskDraftState.reminder as Reminder).toDisplayText(),
                onClick = onShowReminderDialog,
                isDismissible = taskDraftState.repeat == null,
                onDismiss = { taskDraftState.updateReminder(null) },
                icon = schedule
            )
        }

        // --------------------- Repeat -------------------------- //
        if (taskDraftState.repeat != null) {
            FieldChip(
                label = (taskDraftState.repeat as Repeat).toDisplayText(),
                onClick = { navigateToRepeatForm(taskDraftState.reminder) },
                isDismissible = true,
                onDismiss = { taskDraftState.updateRepeat(null) },
                icon = repeat
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            /* Add Details Button */
            Icon(
                menu_open,
                null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = {
                            showDetailsField = true
                            detailsFocusRequester.requestFocus()
                        }
                    )
            )

            Icon(
                schedule,
                null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = onShowReminderDialog
                    )
            )

            Icon(
                star,
                null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = {}
                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            /* Save Button */
            TextButton(
                onClick = {
                    onAddTask()
                    onDismiss()
                },
                enabled = !taskDraftState.isEmpty,
            ) {
                Text("Save")
            }
        }

    }
}

@Preview
@Composable
private fun QuickAddSheetContentPrev() {
    AppTheme {
        QuickAddSheetContent(
            taskDraftState = TaskDraftState(
                reminder = Reminder(
                    date = LocalDate.now(),
                    time = null
                ),
                repeat = Repeat(RepeatType.Day)
            ),
            titleFocusRequester = remember { FocusRequester() },
            detailsFocusRequester = remember { FocusRequester() },
            onDismiss = {},
            onAddTask = {},
            navigateToRepeatForm = {},
            onShowReminderDialog = {},
            modifier = Modifier
        )
    }
}