package com.example.todo.models

import kotlinx.serialization.Serializable
import java.time.LocalDate

data class Task(
    val id: Long = 0,
    val title: String = "",
    val details: String? = null,
    val category: Category? = null,
    val completed: Boolean = false,
    val createdAt: LocalDate = LocalDate.now(),
    val dueDate: LocalDate? = null,
    val repeat: Repeat? = null
)

enum class Category {
    WORK,
    PERSONAL,
    SHOPPING,
    HEALTH,
    STUDY,
    OTHER
}
