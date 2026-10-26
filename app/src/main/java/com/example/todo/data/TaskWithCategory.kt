package com.example.todo.data

import androidx.room3.Embedded
import androidx.room3.Relation
import com.example.todo.models.Task

data class TaskWithCategory(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumns = ["categoryId"],
        entityColumns = ["id"]
    )
    val category: CategoryEntity?
)

fun TaskWithCategory.toDomain() = Task(
    id = task.id,
    title = task.title,
    details = task.details,
    completed = task.completed,
    completedAt = task.completedAt,
    starred = task.starred,
    schedule = task.schedule,
    position = task.position,
    category = category?.toDomain()
)

fun List<TaskWithCategory>.toDomain() = map { it.toDomain() }