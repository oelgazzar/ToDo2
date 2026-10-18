package com.example.todo.data

import com.example.todo.models.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getTask(it: Long): Flow<Task?>
    suspend fun upsertTask(task: Task)
    suspend fun insertAll(tasks: List<Task>)
    suspend fun deleteTask(taskId: Long)
}