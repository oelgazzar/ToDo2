package com.example.todo.ui.utils

import com.example.todo.models.Repeat
import com.example.todo.ui.repeat.MonthDayMode
import com.example.todo.ui.repeat.RepeatEndMode
import com.example.todo.ui.repeat.RepeatType
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

fun Repeat.toDisplayText(): String {
    val dateFormatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)

    val timeFormatter = DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)

    val recurrence = when (repeatType) {
        RepeatType.Day -> {
            if (interval == 1) "Every day"
            else "Every $interval days"
        }

        RepeatType.Week -> {
            val prefix = if (interval == 1) "Every week"
            else "Every $interval weeks"

            val days = daysOfWeek
                ?.sortedBy { it.value }
                ?.joinToString(", ") {
                    it.getDisplayName(TextStyle.FULL, Locale.getDefault())
                }

            if (days.isNullOrBlank()) prefix
            else "$prefix on $days"
        }

        RepeatType.Month -> {
            val prefix = if (interval == 1) "Every month"
            else "Every $interval months"

            when (monthDayMode) {
                MonthDayMode.Day -> {
                    dayOfMonth?.let {
                        "$prefix on the ${it.toOrdinal()}"
                    } ?: prefix
                }

                MonthDayMode.Weekday -> {
                    val nth = nthDayOfWeekInMonth
                    val day = dayOfWeekInMonth

                    if (nth != null && day != null) {
                        "$prefix on the ${nth.toOrdinal()} ${
                            day.getDisplayName(TextStyle.FULL, Locale.getDefault())
                        }"
                    } else {
                        prefix
                    }
                }

                null -> prefix
            }
        }

        RepeatType.Year -> {
            if (interval == 1) "Every year"
            else "Every $interval years"
        }
    }

    val withTime = time?.let {
        "$recurrence at ${it.format(timeFormatter)}"
    } ?: recurrence

    val withStart = startDate?.let {
        "$withTime, starting ${it.format(dateFormatter)}"
    } ?: withTime

    return when (repeatEndMode) {
        RepeatEndMode.Never -> withStart

        RepeatEndMode.OnDate -> {
            endDate?.let {
                "$withStart, until ${it.format(dateFormatter)}"
            } ?: withStart
        }

        RepeatEndMode.AfterNOccurrences -> {
            endAfter?.let {
                "$withStart, for $it occurrences"
            } ?: withStart
        }
    }
}

private fun Int.toOrdinal(): String {
    val suffix = when {
        this % 100 in 11..13 -> "th"
        this % 10 == 1 -> "st"
        this % 10 == 2 -> "nd"
        this % 10 == 3 -> "rd"
        else -> "th"
    }

    return "$this$suffix"
}