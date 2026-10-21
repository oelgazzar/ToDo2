package com.example.todo.data

import androidx.room3.ColumnTypeConverter
import com.example.todo.ui.repeat.RepeatType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class Converters {
    @ColumnTypeConverter
    fun fromRepeatType(value: RepeatType): String = value.name

    @ColumnTypeConverter
    fun toRepeatType(value: String): RepeatType =
        RepeatType.valueOf(value)

    @ColumnTypeConverter
    fun fromLocalDate(value: LocalDate?): String? =
        value?.toString()

    @ColumnTypeConverter
    fun toLocalDate(value: String?): LocalDate? =
        value?.let(LocalDate::parse)

    @ColumnTypeConverter
    fun fromLocalTime(value: LocalTime?): String? =
        value?.toString()

    @ColumnTypeConverter
    fun toLocalTime(value: String?): LocalTime? =
        value?.let(LocalTime::parse)

    @ColumnTypeConverter
    fun fromDayOfWeek(value: DayOfWeek?): String? =
        value?.name

    @ColumnTypeConverter
    fun toDayOfWeek(value: String?): DayOfWeek? =
        value?.let(DayOfWeek::valueOf)

    @ColumnTypeConverter
    fun fromDaysOfWeek(value: Set<DayOfWeek>?): String? =
        value?.joinToString(",") { it.name }

    @ColumnTypeConverter
    fun toDaysOfWeek(value: String?): Set<DayOfWeek>? =
        value?.takeIf { it.isNotEmpty() }
            ?.split(",")
            ?.map { DayOfWeek.valueOf(it) }
            ?.toSet()
}