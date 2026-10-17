package com.example.todo.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.todo.models.Category
import com.example.todo.models.Task
import java.time.Instant
import java.time.LocalDate

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val category: Category? = null,
    val completed: Boolean,
    val dueDate: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        category = category,
        completed = completed,
        dueDate = dueDate?.let { LocalDate.parse(it) },
        createdAt = Instant.ofEpochMilli(createdAt)
    )
}

fun List<TaskEntity>.toDomain(): List<Task> = map { it.toDomain() }

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        category = category,
        completed = completed,
        dueDate = dueDate?.toString(),
        createdAt = createdAt?.toEpochMilli() ?: System.currentTimeMillis()
    )
}

fun List<Task>.toEntity() = map { it.toEntity() }
