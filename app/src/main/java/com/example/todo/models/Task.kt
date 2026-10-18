package com.example.todo.models

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Task(
    val id: Long = 0,
    val title: String = "",
    val details: String? = null,
    val category: Category? = null,
    val completed: Boolean = false,
    @Serializable(with = LocalDateSerializer::class)
    val createdAt: LocalDate = LocalDate.now(),
    @Serializable(with = LocalDateSerializer::class)
    val dueDate: LocalDate? = null,
)

@Serializable
enum class Category {
    WORK,
    PERSONAL,
    SHOPPING,
    HEALTH,
    STUDY,
    OTHER
}
