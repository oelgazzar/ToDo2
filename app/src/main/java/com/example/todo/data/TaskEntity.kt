package com.example.todo.data

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.todo.models.Category
import com.example.todo.models.Repeat
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
    @Embedded(prefix = "repeat_")
    val repeat: Repeat? = null
)

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        details = details,
        category = category,
        completed = completed,
        dueDate = dueDate?.let { LocalDate.parse(it) },
        createdAt = LocalDate.parse(createdAt),
        repeat = repeat
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
        dueDate = dueDate?.toString(),
        createdAt = createdAt.toString(),
        repeat = repeat
    )
}

fun List<Task>.toEntity() = map { it.toEntity() }
