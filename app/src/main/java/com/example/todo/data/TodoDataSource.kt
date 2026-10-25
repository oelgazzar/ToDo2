package com.example.todo.data

import com.example.todo.models.Schedule
import com.example.todo.models.Task
import java.time.LocalDate

val today: LocalDate = LocalDate.now()


val sampleTodos = listOf(
    Task(
        id = 1,
        title = "Submit report",
        position = -1,
        schedule = Schedule(
            dueDate = LocalDate.now()
        )
    ),
    Task(
        id = 2,
        title = "Call Ahmed",
        position = -1
    ),
    Task(
        id = 3,
        title = "Study Kotlin",
        position = -1,
        schedule = Schedule(
            dueDate = LocalDate.now().plusDays(1)
        )
    ),
    Task(
        id = 4,
        title = "Go to gym",
        completed = true,
        position = -1
    ),
    Task(
        id = 5,
        title = "Finish thesis",
        position = -1,
        schedule = Schedule(
            dueDate = LocalDate.now().plusDays(1)
        )
    ),
    Task(
        id = 6,
        title = "Buy groceries",
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