package com.example.todo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TaskRepository
import com.example.todo.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val tasks: List<Task>) : HomeUiState
    data class Error(val message: String) : HomeUiState
    object Loading : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TaskRepository,
) : ViewModel() {
    @OptIn(FlowPreview::class)
    val uiState: StateFlow<HomeUiState> = repository.getAllTasks()
        .map<List<Task>, HomeUiState> {
            HomeUiState.Success(it)
        }
        .catch {
            emit(HomeUiState.Error(it.message ?: "Unknown Error"))
        }
        .stateIn(
            initialValue = HomeUiState.Loading,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.upsertTask(task)
        }
    }

    fun toggleCompleteTask(taskId: Long) {
        viewModelScope.launch {
            repository.toggleCompleted(taskId)
        }
    }

    fun undoDelete(task: Task) {
            addTask(task)
    }
}