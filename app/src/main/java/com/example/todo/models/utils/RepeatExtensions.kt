package com.example.todo.models.utils

import com.example.todo.models.Repeat
import com.example.todo.ui.repeat.MonthDayMode
import com.example.todo.ui.repeat.RepeatEndMode
import com.example.todo.ui.repeat.RepeatType
import java.time.*
import java.time.temporal.TemporalAdjusters

private val DEFAULT_REPEAT_TIME = LocalTime.of(9, 0)

fun Repeat.nextOccurrence(
    after: LocalDateTime = LocalDateTime.now(),
    defaultTime: LocalTime = DEFAULT_REPEAT_TIME
): LocalDateTime? {
    val at = time ?: defaultTime
    return occurrenceDates()
        .map { LocalDateTime.of(it, at) }
        .firstOrNull { it.isAfter(after) }
}

private fun Repeat.occurrenceDates(): Sequence<LocalDate> {
    val start = startDate ?: return emptySequence()
    val step = interval.coerceAtLeast(1).toLong()

    val all: Sequence<LocalDate> = when (repeatType) {
        RepeatType.Day -> generateSequence(0L) { it + 1 }.map { start.plusDays(it * step) }
        RepeatType.Week -> weeklyDates(start, step)
        RepeatType.Month -> monthlyDates(start, step)
        RepeatType.Year -> generateSequence(0L) { it + 1 }.map { start.plusYears(it * step) }
    }

    return when (repeatEndMode) {
        RepeatEndMode.Never -> all
        RepeatEndMode.OnDate -> endDate?.let { end -> all.takeWhile { !it.isAfter(end) } } ?: all
        RepeatEndMode.AfterNOccurrences -> all.take(endAfter ?: Repeat.DEFAULT_END_AFTER)
    }
}

private fun Repeat.weeklyDates(start: LocalDate, step: Long): Sequence<LocalDate> {
    val days = (daysOfWeek?.takeIf { it.isNotEmpty() } ?: setOf(start.dayOfWeek))
        .sortedBy { it.value }
    val firstWeek = start.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    return generateSequence(0L) { it + 1 }
        .flatMap { k ->
            val weekStart = firstWeek.plusWeeks(k * step)
            days.asSequence().map { weekStart.plusDays((it.value - 1).toLong()) }
        }
        .filter { !it.isBefore(start) }
}

private fun Repeat.monthlyDates(start: LocalDate, step: Long): Sequence<LocalDate> =
    generateSequence(0L) { it + 1 }
        .map { k ->
            val ym = YearMonth.from(start).plusMonths(k * step)
            when (monthDayMode) {
                MonthDayMode.Weekday -> {
                    val dow = dayOfWeekInMonth ?: start.dayOfWeek
                    val nth = nthDayOfWeekInMonth ?: 1
                    val first = ym.atDay(1)
                    if (nth in 1..4) first.with(TemporalAdjusters.dayOfWeekInMonth(nth, dow))
                    else first.with(TemporalAdjusters.lastInMonth(dow)) // 5 or -1 = last
                }
                else -> ym.atDay(minOf(dayOfMonth ?: start.dayOfMonth, ym.lengthOfMonth()))
            }
        }
        .filter { !it.isBefore(start) }