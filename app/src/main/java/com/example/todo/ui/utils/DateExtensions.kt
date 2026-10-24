package com.example.todo.ui.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.todo.models.Reminder
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

fun LocalDate.toDueBucket(today: LocalDate = LocalDate.now()): String {
    val daysInterval = ChronoUnit.DAYS.between(this, today).toInt()

    return when {
        daysInterval == -1 -> "Due Tomorrow"
        daysInterval == 0 -> "Due Today"
        daysInterval > 1 -> "Overdue"
        else -> "Due " + this.toFormattedString()
    }
}

fun LocalDateTime.toDisplayText(today: LocalDate = LocalDate.now()): String {
    return "${toLocalDate().toDisplayText(today)}, ${toLocalTime().toFormattedString()}"
}

fun Reminder.toDisplayText(today: LocalDate = LocalDate.now()) =
    this.toLocalDateTime().toDisplayText(today)

fun LocalDate.toReminderBucket(today: LocalDate = LocalDate.now()): String {
    val daysInterval = ChronoUnit.DAYS.between(this, today).toInt()

    return when {
        daysInterval == -1 -> "Tomorrow"
        daysInterval == 0 -> "Today"
        daysInterval > 0 -> "Past"
        else -> this.toFormattedString()
    }
}

val LocalDate.isPast: Boolean
    get() = this.isBefore(LocalDate.now())

val Reminder.isPast: Boolean
    get() = this.date.isPast

val LocalDate.isToday: Boolean
    get() = this == LocalDate.now()

val Reminder.isToday: Boolean
    get() = this.date.isToday

fun LocalDate.getDisplayColor(colorScheme: ColorScheme): Color {
    return when {
        this.isToday -> colorScheme.primary
        this.isPast -> colorScheme.error
        else -> colorScheme.onSurface
    }
}

fun Reminder.getDisplayColor(colorScheme: ColorScheme) = date.getDisplayColor(colorScheme)

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