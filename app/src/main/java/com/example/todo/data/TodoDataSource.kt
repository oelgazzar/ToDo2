package com.example.todo.data

import com.example.todo.models.Category
import com.example.todo.models.Schedule
import com.example.todo.models.Task
import java.time.LocalDate

val today: LocalDate = LocalDate.now()


val sampleTodos = listOf(
    Task(
        id = 1,
        title = "Submit report",
        category = Category.WORK,
        position = -1,
        schedule = Schedule(
            dueDate = LocalDate.now()
        )
    ),
    Task(
        id = 2,
        title = "Call Ahmed",
        category = Category.PERSONAL,
        position = -1
    ),
    Task(
        id = 3,
        title = "Study Kotlin",
        category = Category.STUDY,
        position = -1,
        schedule = Schedule(
            dueDate = LocalDate.now().plusDays(1)
        )
    ),
    Task(
        id = 4,
        title = "Go to gym",
        category = Category.PERSONAL,
        completed = true,
        position = -1
    ),
    Task(
        id = 5,
        title = "Finish thesis",
        category = Category.STUDY,
        position = -1,
        schedule = Schedule(
            dueDate = LocalDate.now().plusDays(1)
        )
    ),
    Task(
        id = 6,
        title = "Buy groceries",
        category = Category.PERSONAL,
        position = -1
    ),
    Task(
        id = 7,
        title = "Read article",
        position = -1,
        schedule = Schedule(
            dueDate = LocalDate.now().plusDays(2)
        )
    )
)