package com.example.todo.data

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import com.example.todo.models.Category
import com.example.todo.models.Schedule
import com.example.todo.models.Task
import java.time.LocalDate

@Entity(
    tableName = "task",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.SET_NULL
    )]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val details: String? = null,
    val categoryId: Long? = null,
    val completed: Boolean = false,
    val dueDate: String? = null,
    val createdAt: String = LocalDate.now().toString(),
    @Embedded(prefix = "schedule_")
    val schedule: Schedule = Schedule(),
    val position: Long
)

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        details = details,
        completed = completed,
        createdAt = createdAt.toString(),
        schedule = schedule,
        position = position,
        categoryId = category?.id
    )
}

fun List<Task>.toEntity() = map { it.toEntity() }
