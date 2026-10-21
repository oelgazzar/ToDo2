package com.example.todo.fakes

import com.example.todo.data.TaskRepository
import com.example.todo.models.Schedule
import com.example.todo.models.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds

class FakeTaskRepository(
    initialTasks: List<Task> = emptyList()
) : TaskRepository {

    private val tasks = MutableStateFlow(initialTasks)

    override fun getAllTasks(): Flow<List<Task>> =
        tasks.asStateFlow()

    override fun getTask(it: Long): Flow<Task?> =
        tasks.map { list ->
            list.find { task -> task.id == it }
        }

    override suspend fun upsertTask(task: Task) {
        tasks.update { list ->
            val exists = list.any { it.id == task.id }

            if (exists) {
                list.map { existing ->
                    if (existing.id == task.id) task else existing
                }
            } else {
                list + task
            }
        }
    }

    override suspend fun insertAll(tasks: List<Task>) {
        tasks.forEach { upsertTask(it) }
    }

    override suspend fun deleteTask(taskId: Long) {
        tasks.update { list ->
            list.filterNot { it.id == taskId }
        }
    }

    override suspend fun updateTaskContent(
        taskId: Long,
        title: String,
        details: String
    ) {
        tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) {
                    task.copy(
                        title = title,
                        details = details
                    )
                } else {
                    task
                }
            }
        }
    }

    override suspend fun toggleCompleted(taskId: Long) {
        tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) {
                    task.copy(completed = !task.completed)
                } else {
                    task
                }
            }
        }
    }

    override suspend fun updateDueDate(taskId: Long, date: LocalDate?) {
        tasks.update { list ->
            list.map { task ->
                if (task.id == taskId) {
                    val schedule = Schedule(dueDate = date)
                    task.copy(schedule = schedule)
                } else {
                    task
                }
            }
        }
    }
}