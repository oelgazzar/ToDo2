package com.example.todo.ui.home

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.todo.models.Reminder
import com.example.todo.models.Repeat
import com.example.todo.models.Schedule
import com.example.todo.models.Task

@Stable
class TaskDraftState(
    val title: TextFieldState = TextFieldState(),
    val details: TextFieldState = TextFieldState(),
    reminder: Reminder? = null,
    repeat: Repeat? = null,
) {
    val isEmpty: Boolean
        get() = title.text.isBlank()
                && details.text.isBlank()
                && reminder == null
                && repeat == null

    var reminder: Reminder? by mutableStateOf(reminder)
        private set

    var repeat: Repeat? by mutableStateOf(repeat)
        private set

    fun updateReminder(reminder: Reminder?) {
        this.reminder = reminder
    }

    fun updateRepeat(repeat: Repeat?) {
        this.repeat = repeat
    }
}

fun TaskDraftState.toTask() = Task(
    title = title.text.toString(),
    details = details.text.toString().takeIf { it.isNotBlank() },
    schedule = Schedule(
        reminder = reminder,
        repeat = repeat
    ),
)