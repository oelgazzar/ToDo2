package com.example.todo.models

import java.time.LocalDate

data class Task(
    val id: Long = 0,
    val title: String = "",
    val details: String? = null,
    val category: Category? = null,
    val completed: Boolean = false,
    val createdAt: LocalDate = LocalDate.now(),
    val schedule: Schedule = Schedule(),
    val position: Long
) {
    val isRepeatable: Boolean
        get() = schedule.repeat != null
}