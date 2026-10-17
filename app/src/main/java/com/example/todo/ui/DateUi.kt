package com.example.todo.ui

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun LocalDate.toDisplayText(today: LocalDate = LocalDate.now()): String {
    val daysInterval = ChronoUnit.DAYS.between(today, this)
    val formatter = DateTimeFormatter.ofPattern("EEE, MMM d")
    val days = if (-daysInterval == 1L) "day" else "days"

    return when (daysInterval) {
        0L -> "Today"
        1L -> "Tomorrow"
        in -1L downTo -3L -> "${-daysInterval} $days ago"
        else -> this.format(formatter)
    }
}
