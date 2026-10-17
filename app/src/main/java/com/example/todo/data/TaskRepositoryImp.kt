package com.example.todo.data

import com.example.todo.models.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun upsertTask(task: Task)
    suspend fun insertAll(tasks: List<Task>)
}

class TaskRepositoryImp(
    private val taskDao: TaskDao
): TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { it.toDomain() }
    }

    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task.toEntity())
    }

    override suspend fun insertAll(tasks: List<Task>) {
        taskDao.insertTasks(tasks.toEntity())
    }
}