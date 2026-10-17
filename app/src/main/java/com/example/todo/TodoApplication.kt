package com.example.todo

import android.app.Application
import android.content.Context
import com.example.todo.data.AppDatabase
import com.example.todo.data.TaskRepository
import com.example.todo.data.TaskRepositoryImp

class AppContainer(context: Context) {
    val taskRepository: TaskRepository by lazy {
        val taskDao = AppDatabase.getDatabase(context).taskDao()
        TaskRepositoryImp(taskDao)
    }
}

class TodoApplication: Application() {
    val container = AppContainer(this)
}