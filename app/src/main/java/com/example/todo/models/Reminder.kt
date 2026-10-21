package com.example.todo.models

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class Reminder(
    val date: LocalDate,
    val time: LocalTime? = null
) {
    override fun toString(): String {
        return time?.let { LocalDateTime.of(date, time).toString() }?: date.toString()
    }

    companion object {
        fun fromString(string: String): Reminder {
            return runCatching {
                val dt = LocalDateTime.parse(string)
                Reminder(
                    dt.toLocalDate(),
                    dt.toLocalTime()
                )
            }.getOrNull() ?: Reminder(
                LocalDate.parse(string)
            )
        }
    }
}

fun Reminder.toLocalDateTime(): LocalDateTime = LocalDateTime.of(date, time?: LocalTime.now())

fun LocalDateTime.toReminder() = Reminder(date = toLocalDate(), time = toLocalTime())