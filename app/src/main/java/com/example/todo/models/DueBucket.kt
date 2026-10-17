package com.example.todo.models

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

enum class DueBucket {
    OVERDUE,
    TODAY,
    TOMORROW,
    THIS_WEEK,
    NEXT_WEEK,
    THIS_MONTH,
    LATER,
    NO_DUE_DATE
}

fun LocalDate?.toDueBucket(today: LocalDate = LocalDate.now()): DueBucket {
    if (this == null) return DueBucket.NO_DUE_DATE

    val tomorrow = today.plusDays(1)
    val nextWeekStart = today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY))
    val followingWeekStart = nextWeekStart.plusWeeks(1)
    val nextMonthStart = today.with(TemporalAdjusters.firstDayOfNextMonth())

    return when {
        this < today -> DueBucket.OVERDUE
        this == today -> DueBucket.TODAY
        this == tomorrow -> DueBucket.TOMORROW
        this < nextWeekStart -> DueBucket.THIS_WEEK
        this < followingWeekStart -> DueBucket.NEXT_WEEK
        this < nextMonthStart -> DueBucket.THIS_MONTH
        else -> DueBucket.LATER
    }
}
