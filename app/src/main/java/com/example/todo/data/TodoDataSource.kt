package com.example.todo.data

import com.example.todo.models.Category
import com.example.todo.models.Task
import java.time.LocalDate

val today: LocalDate = LocalDate.now()


val sampleTodos = listOf(
    Task(
        id = 1,
        title = "Submit report",
        category = Category.WORK,
        completed = false,
        dueDate = today.minusDays(2),
        createdAt = LocalDate.now()
    ),
    Task(
        id = 2,
        title = "Call Ahmed",
        category = Category.PERSONAL,
        completed = false,
        dueDate = today.minusDays(1),
        createdAt = LocalDate.now()
    ),
    Task(
        id = 3,
        title = "Study Kotlin",
        category = Category.STUDY,
        completed = false,
        dueDate = today,
        createdAt = LocalDate.now()
    ),
    Task(
        id = 4,
        title = "Go to gym",
        category = Category.PERSONAL,
        completed = true,
        dueDate = today,
        createdAt = LocalDate.now()
    ),
    Task(
        id = 5,
        title = "Finish thesis",
        category = Category.STUDY,
        completed = false,
        dueDate = today.plusDays(1),
        createdAt = LocalDate.now()
    ),
    Task(
        id = 6,
        title = "Buy groceries",
        category = Category.PERSONAL,
        completed = false,
        dueDate = today.plusDays(5),
        createdAt = LocalDate.now()
    ),
    Task(
        id = 7,
        title = "Read article",
        category = null,
        completed = false,
        dueDate = null,
        createdAt = LocalDate.now()
    )
)