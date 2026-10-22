package com.example.todo.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.check
import com.example.test.radio_button_unchecked
import com.example.test.repeat
import com.example.test.schedule
import com.example.test.target_check
import com.example.todo.models.Reminder
import com.example.todo.models.Repeat
import com.example.todo.models.Schedule
import com.example.todo.models.Task
import com.example.todo.models.toLocalDateTime
import com.example.todo.ui.repeat.RepeatType
import com.example.todo.ui.theme.AppTheme
import com.example.todo.ui.utils.toDisplayText
import com.example.todo.ui.utils.toUi
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskItem(
    task: Task,
    onTaskClicked: () -> Unit,
    onToggleCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryUi = task.category?.toUi()

    Surface(
        color = CardDefaults.cardColors().containerColor,
        modifier = modifier
            .clickable(onClick = onTaskClicked)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    if (task.completed) check else radio_button_unchecked,
                    null,
                    tint = if (task.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            onClick = { onToggleCompleted() }
                        )
                        .testTag("task_completed_checkbox")
                )

                Text(
                    text = task.title,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else null,
                    modifier = Modifier
                        .padding(start = 16.dp)
                )
            }

            task.details?.ifBlank { null }?.let { details ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(start = 40.dp)
                ) {
                    Text(
                        details,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    //                categoryUi?.icon?.let {
                    //                    Icon(
                    //                        it,
                    //                        null,
                    //                        tint = categoryUi.color,
                    //                        modifier = Modifier.size(15.dp)
                    //                            .padding(end = 4.dp)
                    //                    )
                    //                }
                    //
                    //                Text(
                    //                    text = category.name,
                    //                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    //                    style = MaterialTheme.typography.labelMedium,
                    //                )
                }
            }

            task.schedule.dueDate?.let { dueDate ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(start = 40.dp)
                ) {
                    Icon(
                        target_check,
                        null,
                        modifier = Modifier
                            .size(18.dp)
                    )
                    Text(
                        text = "Due ${dueDate.toDisplayText()}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            task.schedule.reminder?.let { reminder ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(start = 40.dp)
                ) {
                    Icon(
                        schedule,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(18.dp)
                    )
                    Text(
                        text = reminder.toLocalDateTime().toDisplayText(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    task.schedule.repeat?.let {
                        Icon(
                            repeat, null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(16.dp)

                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskItemPrev() {
    AppTheme {
        TaskItem(
            task = Task(
                title = "Task 1",
                schedule = Schedule(
                    dueDate = LocalDate.now(),
                    reminder = Reminder
                        (
                        LocalDate.now()
                            .plusDays(1),
                        LocalTime.of(9, 25)
                    ),
                    repeat = Repeat(RepeatType.Day),
                ),
                position = -1,
            ),
            onTaskClicked = {},
            onToggleCompleted = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun CompletedTaskItemPrev() {
    AppTheme {
        TaskItem(
            task = Task(
                title = "Task 1",
                details = "First task",
                completed = true,
                schedule = Schedule(
                    dueDate = LocalDate.now()
                ),
                position = -1
            ),
            onTaskClicked = {},
            onToggleCompleted = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun Test() {
    RadioButton(
        selected = true,
        onClick = { },
        modifier = Modifier.size(20.dp)
    )
}

