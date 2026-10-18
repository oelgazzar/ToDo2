package com.example.todo.data

import com.example.todo.models.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImp @Inject constructor(
    private val taskDao: TaskDao
): TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { it.toDomain() }
    }

    override fun getTask(it: Long): Flow<Task?> = taskDao.getTask(it).map { it?.toDomain() }

    override suspend fun upsertTask(task: Task) {
        taskDao.upsertTask(task.toEntity())
    }

    override suspend fun insertAll(tasks: List<Task>) {
        taskDao.insertTasks(tasks.toEntity())
    }

    override suspend fun deleteTask(taskId: Long) = taskDao.deleteTask(taskId)
}