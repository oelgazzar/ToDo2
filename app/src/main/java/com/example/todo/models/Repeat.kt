package com.example.todo.models

import com.example.todo.ui.repeat.MonthDayMode
import com.example.todo.ui.repeat.RepeatEndMode
import com.example.todo.ui.repeat.RepeatType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

data class Repeat(
    val repeatType: RepeatType,
    val interval: Int = 1,
    val daysOfWeek: Set<DayOfWeek>? = null,
    val monthDayMode: MonthDayMode? = null,
    val dayOfMonth: Int? = null,
    val nthDayOfWeekInMonth: Int? = null,
    val dayOfWeekInMonth: DayOfWeek? = null,
    val time: LocalTime? = null,
    val startDate: LocalDate? = null,
    val repeatEndMode: RepeatEndMode = RepeatEndMode.Never,
    val endDate: LocalDate? = null,
    val endAfter: Int? = null
) {
    companion object {
        const val DEFAULT_INTERVAL = 1
        const val DEFAULT_END_AFTER = 10
    }
}