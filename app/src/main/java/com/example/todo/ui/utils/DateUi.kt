package com.example.todo.ui.utils

import com.example.todo.models.Reminder
import com.example.todo.models.Repeat
import com.example.todo.models.toLocalDateTime
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM d")
private val fullFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, h:mm a")

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

fun LocalDateTime.toDisplayText(today: LocalDate = LocalDate.now()): String {
    return "${toLocalDate().toDisplayText(today)}, ${toLocalTime().toFormattedString()}"
}

fun Reminder.toDisplayText(today: LocalDate = LocalDate.now()) = this.toLocalDateTime().toDisplayText(today)

fun LocalDate.toFormattedString(): String {
    return this.format(dateFormatter)
}

fun LocalTime.toFormattedString(): String {
    return this.format(timeFormatter)
}

fun LocalDateTime.toFormattedString(): String {
    return this.format(fullFormatter)
}

fun Reminder.toFormattedString(): String {
    return if (time != null) {
        this.toLocalDateTime().toFormattedString()
    } else {
        this.date.toFormattedString()
    }
}