package com.example.todo.ui.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM d")

fun LocalDate.toDisplayText(today: LocalDate = LocalDate.now()): String {
    val daysInterval = ChronoUnit.DAYS.between(this, today).toInt()

    return when {
        daysInterval == -1 -> "Tomorrow"
        daysInterval == 0 -> "Today"
        daysInterval == 1 -> "Yesterday"
        daysInterval in 2..6 -> "$daysInterval days ago"
        daysInterval in 7..30 -> "${daysInterval / 7} week${if (daysInterval / 7 > 1) "s" else ""} ago"
        daysInterval in 30..364 -> "${daysInterval / 30} month${if (daysInterval / 30 > 1) "s" else ""} ago"
        daysInterval >= 365 -> "${daysInterval / 365} year${if (daysInterval / 365 > 1) "s" else ""} ago"
        else -> this.toFormattedString()
    }
}

fun LocalDate.toFormattedString(): String {
    return this.format(formatter)
}
