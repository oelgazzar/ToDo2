package com.example.todo.models

import java.time.Instant
import java.time.LocalDate

data class Task(
    val id: Long,
    val title: String,
    val category: Category?,
    val completed: Boolean,
    val dueDate: LocalDate?,
    val createdAt: Instant?,
)

enum class Category {
    WORK,
    PERSONAL,
    SHOPPING,
    HEALTH,
    STUDY,
    OTHER
}
