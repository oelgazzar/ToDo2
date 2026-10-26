package com.example.todo.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.check
import com.example.test.radio_button_unchecked
import com.example.test.repeat
import com.example.test.schedule
import com.example.test.star
import com.example.test.star_filled
import com.example.test.target_check
import com.example.todo.models.Reminder
import com.example.todo.models.Repeat
import com.example.todo.models.Schedule
import com.example.todo.models.SortType
import com.example.todo.models.Task
import com.example.todo.models.toLocalDateTime
import com.example.todo.models.utils.toColor
import com.example.todo.ui.components.ColorDot
import com.example.todo.ui.components.FieldChip
import com.example.todo.ui.repeat.RepeatType
import com.example.todo.ui.theme.AppTheme
import com.example.todo.ui.utils.getDisplayColor
import com.example.todo.ui.utils.toDisplayText
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskItem(
    task: Task,
    onTaskClicked: () -> Unit,
    onToggleCompleted: () -> Unit,
    onStar: () -> Unit,
    sortType: SortType,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
) {
    Surface(
        color = CardDefaults.cardColors().containerColor,
        shape = shape,
        modifier = modifier.clickable(onClick = onTaskClicked)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        if (task.isCompleted) check else radio_button_unchecked,
                        null,
                        tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                onClick = { onToggleCompleted() })
                            .testTag("task_completed_checkbox")
                    )

                    Text(
                        text = task.title,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                        modifier = Modifier.padding(start = 16.dp)
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
                            details, style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                if (sortType != SortType.CATEGORY) {
                    task.category?.let { c ->
                        FieldChip(
                            label = c.name, onClick = {}, isDismissible = false, iconContent = {
                                c.icon?.let {
                                    Icon(
                                        c.icon.icon,
                                        null,
                                        tint = c.color?.toColor() ?: Color.Unspecified
                                    )
                                } ?: c.color?.let {
                                    ColorDot(
                                        c.color.toColor()
                                    )
                                }
                            }, modifier = Modifier.padding(start = 40.dp)
                        )
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
                            tint = dueDate.getDisplayColor(MaterialTheme.colorScheme),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Due ${dueDate.toDisplayText()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = dueDate.getDisplayColor(MaterialTheme.colorScheme),
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
                            tint = reminder.getDisplayColor(MaterialTheme.colorScheme),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = reminder.toLocalDateTime().toDisplayText(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = reminder.getDisplayColor(MaterialTheme.colorScheme),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        task.schedule.repeat?.let {
                            Icon(
                                repeat,
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .size(16.dp)

                            )
                        }
                    }
                }
            }

            IconButton(onClick = onStar) {
                Icon(if (task.starred) star_filled else star, null)
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
                    reminder = Reminder(
                        LocalDate.now().plusDays(1), LocalTime.of(9, 25)
                    ),
                    repeat = Repeat(RepeatType.Day),
                ),
                position = -1,
            ),
            onTaskClicked = {},
            onToggleCompleted = {},
            onStar = {},
            shape = CardDefaults.shape,
            sortType = SortType.CATEGORY,
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
                title = "Task 1", details = "First task", completed = true, schedule = Schedule(
                    dueDate = LocalDate.now()
                ),
                position = -1,
                starred = true
            ),
            onTaskClicked = {},
            onToggleCompleted = {},
            onStar = {},
            shape = CardDefaults.shape,
            sortType = SortType.CATEGORY,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun Test() {
    RadioButton(
        selected = true, onClick = { }, modifier = Modifier.size(20.dp)
    )
}

