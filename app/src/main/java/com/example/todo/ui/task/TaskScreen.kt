package com.example.todo.ui.task

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test.category
import com.example.test.close
import com.example.test.menu_open
import com.example.test.repeat
import com.example.test.schedule
import com.example.test.target_check
import com.example.todo.models.Category
import com.example.todo.models.Reminder
import com.example.todo.models.Schedule
import com.example.todo.models.Task
import com.example.todo.models.utils.toColor
import com.example.todo.ui.components.ColorDot
import com.example.todo.ui.components.ConfirmDeleteDialog
import com.example.todo.ui.components.DatePickerDialog
import com.example.todo.ui.components.FieldChip
import com.example.todo.ui.components.ReminderDialog
import com.example.todo.ui.components.TransparentTextField
import com.example.todo.ui.shared.TaskActionViewModel
import com.example.todo.ui.theme.AppTheme
import com.example.todo.ui.utils.toDisplayText
import com.example.todo.ui.utils.toFormattedString
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class TaskDetails(val taskId: Long)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    navigateBack: () -> Unit,
    navigateToRepeatForm: (Reminder?) -> Unit,
    viewModel: TaskViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    val currentTask by viewModel.currentTask.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val taskActionViewModel: TaskActionViewModel =
        hiltViewModel(LocalActivity.current as ViewModelStoreOwner)

    BackHandler {
        coroutineScope.launch {
            viewModel.saveTask()
            navigateBack()
        }
    }

    Scaffold(
        topBar = {
            TaskAppBar(
                navigateBack = {
                    coroutineScope.launch {
                        viewModel.saveTask()
                        navigateBack()
                    }
                },
                onDelete = {
                    showConfirmDeleteDialog = true
                }
            )
        }
    ) { innerPadding ->
        TaskScreenContent(
            titleFieldState = viewModel.titleFieldState,
            detailsFieldState = viewModel.detailsFieldState,
            currentTask = currentTask,
            toggleCompleted = {
                coroutineScope.launch {
                    val taskCompleted = viewModel.toggleCompleted()
                    if (taskCompleted) {
                        taskActionViewModel.publishCompletedTask(viewModel.taskId)
                        navigateBack()
                    }
                }
            },
            onDueDateChanged = viewModel::updateDueDate,
            onReminderChanged = viewModel::updateReminder,
            navigateToRepeatForm = navigateToRepeatForm,
            onRepeatRemoved = viewModel::removeRepeat,
            categories = categories,
            onSelectCategory = viewModel::onSelectCategory,
            onCreateCategory = viewModel::onCreateCategory,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        )
    }

    if (showConfirmDeleteDialog) {
        ConfirmDeleteDialog(
            onDismiss = { showConfirmDeleteDialog = false },
            onConfirm = {
                coroutineScope.launch {
                    showConfirmDeleteDialog = false
                    val task = viewModel.deleteTask()
                    task?.let {
                        taskActionViewModel.publishDeletedTask(task)
                    }
                    navigateBack()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreenContent(
    titleFieldState: TextFieldState,
    detailsFieldState: TextFieldState,
    currentTask: Task?,
    toggleCompleted: () -> Unit,
    onDueDateChanged: (LocalDate?) -> Unit,
    onReminderChanged: (Reminder?) -> Unit,
    onRepeatRemoved: () -> Unit,
    navigateToRepeatForm: (Reminder?) -> Unit,
    categories: List<Category>,
    onSelectCategory: (Long?) -> Unit,
    onCreateCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showReminderDialog by remember { mutableStateOf(false) }
    var showCategorySheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        TransparentTextField(
            state = titleFieldState,
            placeholderText = "Enter title",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Icon(
                menu_open,
                null,
            )
            TransparentTextField(
                state = detailsFieldState,
                placeholderText = "Add details",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .alignBy(FirstBaseline)
                    .fillMaxWidth()
            )
        }

        // ------------------- Deadline ------------------------ //

        AnimatedVisibility(currentTask?.isRepeatable == false) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable(
                        onClick = { showDatePicker = true }
                    )
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    target_check,
                    null,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                if (currentTask?.schedule?.dueDate != null) {
                    InputChip(
                        false,
                        onClick = { showDatePicker = true },
                        label = { Text(currentTask.schedule.dueDate.toFormattedString()) },
                        trailingIcon = {
                            Icon(
                                close, null,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onDueDateChanged(null) }
                            )
                        }
                    )
                } else {
                    Text("Add deadline")
                }
            }
        }

        // ---------------- Reminder ----------------------- //

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .clickable(
                    onClick = { showReminderDialog = true }
                )
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                schedule,
                null,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            if (currentTask?.schedule?.reminder != null) {
                FieldChip(
                    label = currentTask.schedule.reminder.toFormattedString(),
                    onClick = { showReminderDialog = true },
                    isDismissible = currentTask.schedule.repeat == null,
                    onDismiss = { onReminderChanged(null) }
                )
            } else {
                Text("Add Reminder")
            }
        }


        // --------------------------------- Repeat -----------------------//
        if (currentTask?.schedule?.repeat != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable(
                        onClick = { navigateToRepeatForm(currentTask.schedule.reminder) }
                    )
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    repeat,
                    null,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                FieldChip(
                    label = currentTask.schedule.repeat!!.toDisplayText(),
                    onClick = { navigateToRepeatForm(currentTask.schedule.reminder) },
                    isDismissible = true,
                    onDismiss = { onRepeatRemoved() }
                )
            }
        }

        // --------------------------------- Category -----------------------//
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .clickable(
                    onClick = { showCategorySheet = true }
                )
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                category,
                null,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            currentTask?.category?.let { c ->
                FieldChip(
                    label = currentTask.category.name,
                    onClick = { showCategorySheet = true },
                    isDismissible = true,
                    onDismiss = { onSelectCategory(null) },
                    iconContent = {
                        c.icon?.let {
                            Icon(c.icon.icon, null, tint = c.color?.toColor() ?: Color.Unspecified)
                        } ?: c.color?.let {
                            ColorDot(
                                c.color.toColor()
                            )
                        }
                    }
                )
            } ?: Text("Add category")
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = toggleCompleted,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (currentTask?.completed == true) "Mark uncompleted" else "Mark completed")
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            initialDate = currentTask?.schedule?.dueDate,
            onConfirm = onDueDateChanged,
            onDismiss = { showDatePicker = false },
        )
    }

    if (showReminderDialog) {
        ReminderDialog(
            initialReminder = currentTask?.schedule?.reminder,
            onConfirm = {
                onReminderChanged(it)
                showReminderDialog = false
            },
            onDismiss = { showReminderDialog = false },
            showRepeatForm = {
                showReminderDialog = false
                navigateToRepeatForm(it)
            }
        )
    }

    if (showCategorySheet) {
        CategorySheet(
            categories = categories,
            selectedId = currentTask?.category?.id,
            onSelect = onSelectCategory,
            onCreate = onCreateCategory,
            onDismiss = { showCategorySheet = false}
        )
    }

}

@Preview
@Composable
private fun TaskScreenContentPrev() {
    AppTheme {
        TaskScreenContent(
            titleFieldState = rememberTextFieldState(),
            detailsFieldState = rememberTextFieldState(),
            currentTask = Task(
                title = "Task 1",
                schedule = Schedule(dueDate = LocalDate.now()),
                position = -1,
            ),
            toggleCompleted = {},
            onDueDateChanged = {},
            onReminderChanged = {},
            navigateToRepeatForm = {},
            onRepeatRemoved = {},
            categories = listOf(),
            onSelectCategory = {},
            onCreateCategory = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}