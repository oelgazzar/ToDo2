package com.example.todo.fakes

import com.example.todo.data.TaskRepository
import com.example.todo.models.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds

class FakeTaskRepository: TaskRepository {
    val tasks = mutableListOf<Task>()
    val f = MutableSharedFlow<List<Task>>()

    override fun getAllTasks(): Flow<List<Task>> = f
    override suspend fun upsertTask(task: Task) {
        tasks.addFirst(task)
        f.emit(tasks)
    }

    override suspend fun insertAll(tasks: List<Task>) {
        TODO("Not yet implemented")
    }
}