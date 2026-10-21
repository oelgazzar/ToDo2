package com.example.todo.ui.repeat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.todo.models.Repeat
import com.example.todo.ui.components.DayOfWeekPickerState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class RepeatFormState {
    var interval by mutableStateOf("1")
        private set

    var repeatType by mutableStateOf(RepeatType.Day)
        private set

    var dayOfWeekPickerState = DayOfWeekPickerState()

    var monthDayMode by mutableStateOf(MonthDayMode.Day)
        private set

    var dayOfMonth by mutableIntStateOf(1)
        private set

    var nthDayOfWeekInMonth by mutableIntStateOf(1)
        private set

    var dayOfWeekInMonth by mutableStateOf(DayOfWeek.SATURDAY)
        private set

    var time by mutableStateOf<LocalTime?>(null)
        private set

    var startDate by mutableStateOf<LocalDate?>(LocalDate.now())
        private set

    var endDate by mutableStateOf<LocalDate?>(LocalDate.now().plusDays(1))
        private set

    var endAfter by mutableStateOf("")
        private set

    var repeatEndMode by mutableStateOf(RepeatEndMode.Never)
        private set

    fun updateInterval(value: String) {
        interval = value
    }

    fun updateRepeatType(value: RepeatType) {
        repeatType = value
    }

    fun updateMonthDayMode(value: MonthDayMode) {
        monthDayMode = value
    }

    fun updateDayOfMonth(value: Int) {
        dayOfMonth = value
    }

    fun updateNthDayOfWeekInMonth(value: Int) {
        nthDayOfWeekInMonth = value
    }

    fun updateDayOfWeekInMonth(value: DayOfWeek) {
        dayOfWeekInMonth = value
    }

    fun updateTime(value: LocalTime?) {
        time = value
    }

    fun updateStartDate(value: LocalDate?) {
        startDate = value
    }

    fun updateEndDate(value: LocalDate?) {
        endDate = value
    }

    fun updateEndAfter(value: String) {
        endAfter = value
    }

    fun updateRepeatEndMode(value: RepeatEndMode) {
        repeatEndMode = value
    }
}

fun RepeatFormState.toRepeat(): Repeat {
    return Repeat(
        repeatType = repeatType,
        interval = interval.toIntOrNull()?: Repeat.DEFAULT_INTERVAL,
        daysOfWeek = dayOfWeekPickerState.selectedDays,
        monthDayMode = monthDayMode,
        dayOfMonth = dayOfMonth,
        nthDayOfWeekInMonth = nthDayOfWeekInMonth,
        dayOfWeekInMonth = dayOfWeekInMonth,
        time = time,
        startDate = startDate,
        repeatEndMode = repeatEndMode,
        endDate = endDate,
        endAfter = endAfter.toIntOrNull()?: Repeat.DEFAULT_END_AFTER
    )
}

fun Repeat.toRepeatFormState(): RepeatFormState {
    return RepeatFormState().apply {
        updateInterval(this@toRepeatFormState.interval.toString())
        updateRepeatType(this@toRepeatFormState.repeatType)
        updateMonthDayMode(this@toRepeatFormState.monthDayMode ?: MonthDayMode.Day)
        updateDayOfMonth(this@toRepeatFormState.dayOfMonth ?: 1)
        updateNthDayOfWeekInMonth(this@toRepeatFormState.nthDayOfWeekInMonth ?: 1)
        updateDayOfWeekInMonth(this@toRepeatFormState.dayOfWeekInMonth ?: DayOfWeek.SATURDAY)
        updateTime(this@toRepeatFormState.time)
        updateStartDate(this@toRepeatFormState.startDate)
        updateEndDate(this@toRepeatFormState.endDate)
        updateEndAfter(this@toRepeatFormState.endAfter.toString())
        updateRepeatEndMode(this@toRepeatFormState.repeatEndMode)
    }
}

enum class RepeatType {
    Day, Week, Month, Year
}

enum class MonthDayMode {
    Day, Weekday
}

enum class RepeatEndMode {
    Never, OnDate, AfterNOccurrences
}
