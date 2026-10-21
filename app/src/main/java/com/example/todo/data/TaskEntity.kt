package com.example.todo.data

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.todo.models.Category
import com.example.todo.models.Repeat
import com.example.todo.models.Schedule
import com.example.todo.models.Task
import java.time.Instant
import java.time.LocalDate

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val details: String? = null,
    val category: Category? = null,
    val completed: Boolean = false,
    val dueDate: String? = null,
    val createdAt: String = LocalDate.now().toString(),
    @Embedded(prefix = "schedule_")
    val schedule: Schedule = Schedule()
)

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        details = details,
        category = category,
        completed = completed,
        createdAt = LocalDate.parse(createdAt),
        schedule = schedule
    )
}

fun List<TaskEntity>.toDomain(): List<Task> = map { it.toDomain() }

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        details = details,
        category = category,
        completed = completed,
        createdAt = createdAt.toString(),
        schedule = schedule
    )
}

fun List<Task>.toEntity() = map { it.toEntity() }
