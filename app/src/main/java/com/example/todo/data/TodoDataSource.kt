package com.example.todo.data

import com.example.todo.models.Category
import com.example.todo.models.CategoryColor
import com.example.todo.models.CategoryIcon
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
val categories = listOf(
    Category(
        id = 1,
        name = "Work",
        color = CategoryColor.RED,
        icon = CategoryIcon.Work,
        position = 1
    ),
    Category(
        id = 2,
        name = "Personal",
        color = CategoryColor.BLUE,
        icon = CategoryIcon.Personal,
        position = 2
    ),
    Category(
        id = 3,
        name = "Study",
        color = CategoryColor.GREEN,
        icon = CategoryIcon.Study,
        position = 3
    ),
    Category(
        id = 4,
        name = "Health",
        color = CategoryColor.ORANGE,
        icon = CategoryIcon.Health,
        position = 4
    ),
    Category(
        id = 5,
        name = "Shopping",
        color = CategoryColor.PURPLE,
        icon = CategoryIcon.Shopping,
        position = 5
    )
)