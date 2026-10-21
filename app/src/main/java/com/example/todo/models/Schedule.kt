package com.example.todo.models

import androidx.room3.Embedded
import java.time.LocalDate

data class Schedule(
    val dueDate: LocalDate? = null,
    @Embedded(prefix = "reminder_")
    val reminder: Reminder? = null,
    @Embedded(prefix = "repeat_")
    val repeat: Repeat? = null
)

