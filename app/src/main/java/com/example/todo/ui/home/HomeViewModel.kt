package com.example.todo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todo.TodoApplication
import com.example.todo.data.TaskRepository
import com.example.todo.data.TaskRepositoryImp
import com.example.todo.models.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TaskRepository
): ViewModel() {
    val tasks = repository.getAllTasks()
        .stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun toggleCompleteTask(task: Task, completed: Boolean) {
        viewModelScope.launch {
            repository.upsertTask(task.copy(completed = completed))
        }
    }

    fun addNewTask(title: String) {
        viewModelScope.launch {
            repository.upsertTask(Task(
                title = title,
                category = null,
                completed = false,
                dueDate = null,
                createdAt = null,
                id = 0
            ))
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TodoApplication)
                    HomeViewModel(application.container.taskRepository)
                }
            }
    }
}