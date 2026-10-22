@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.todo.ui.home

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
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import com.example.todo.ui.theme.AppTheme
import com.example.todo.ui.utils.toDisplayText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TaskFormSheet(
    onDismiss: () -> Unit,
    onAddTask: () -> Unit,
    taskDraftState: TaskDraftState,
    navigateToRepeatForm: (Reminder?) -> Unit,
    modifier: Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var showDetailsField by remember { mutableStateOf(false) }

    val titleFocusRequester = remember { FocusRequester() }
    val detailsFocusRequester =
        remember(showDetailsField) { if (showDetailsField) FocusRequester() else null }
    var showReminderDialog by remember { mutableStateOf(false) }
    var showDiscardTaskConfirmDialog by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
//        confirmValueChange = { targetValue ->
//            if (targetValue == SheetValue.Hidden && !taskDraftState.isEmpty) {
//                showDiscardTaskConfirmDialog = true
//                false
//            } else {
//                true
//            }
//        }
    )

    LaunchedEffect(sheetState.currentValue) {
        if (sheetState.currentValue == SheetValue.Expanded) {
            titleFocusRequester.requestFocus()
        }
    }

    LaunchedEffect(showDetailsField) {
        if (showDetailsField) {
            delay(50.milliseconds)
            detailsFocusRequester?.requestFocus()
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
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
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
                    onClick = { showReminderDialog = true },
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
                                detailsFocusRequester?.requestFocus()
                            }
                        )
                )

                Icon(
                    schedule,
                    null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            onClick = {
                                showReminderDialog = true
                            }

                        )
                )

                Icon(
                    star,
                    null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            onClick = {
                            }

                        )
                )

                Spacer(modifier = Modifier.weight(1f))

                /* Save Button */
                TextButton(
                    onClick = {
                        onAddTask()
                        onDismiss()
                    },
                    enabled = taskDraftState.title.text.isNotBlank(),
                ) {
                    Text("Save")
                }
            }

        }
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

@Preview
@Composable
private fun TaskFormSheetPrev() {
    AppTheme {
        TaskFormSheet(
            onDismiss = {},
            onAddTask = {},
            taskDraftState = TaskDraftState(),
            navigateToRepeatForm = {},
            modifier = Modifier
        )
    }
}

@Preview
@Composable
private fun ContentPrev() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
//                .wrapContentHeight()
            .imePadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        TransparentTextField(
            state = rememberTextFieldState(),
            placeholderText = "New Task",
        )

        Row(
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
                        onClick = {}

                    )
            )

            Spacer(modifier = Modifier.weight(1f))

            /* Save Button */
            TextButton(
                onClick = {
                },
                enabled = false,
            ) {
                Text("Save")
            }
        }

    }
}