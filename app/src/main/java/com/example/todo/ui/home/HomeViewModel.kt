package com.example.todo.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.todo.data.TaskRepository
import com.example.todo.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val tasks: List<Task>) : HomeUiState
    data class Error(val message: String) : HomeUiState
    object Loading : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Task encode by Json
    private var lastDeletedTask = savedStateHandle.toRoute<Home>().deletedTask?.let {
        Json.decodeFromString<Task>(it)
    }

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

    private val _uiEvents = Channel<String>(
        capacity = 1
    )
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        lastDeletedTask?.let {
            _uiEvents.trySend("Task deleted")
        }
    }

    fun toggleCompleteTask(task: Task, completed: Boolean) {
        viewModelScope.launch {
            repository.upsertTask(task.copy(completed = completed))
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.upsertTask(task)
        }
    }

    suspend fun undoDelete() {
        lastDeletedTask?.let {
            addTask(it)
        }
    }

    fun deletePermanently() {
        lastDeletedTask = null
    }
}