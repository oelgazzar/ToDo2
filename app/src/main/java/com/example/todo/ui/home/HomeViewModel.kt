package com.example.todo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.PreferenceRepository
import com.example.todo.data.TaskRepository
import com.example.todo.models.SortType
import com.example.todo.models.Task
import com.example.todo.models.TaskSection
import com.example.todo.models.toTaskSections
import com.example.todo.ui.components.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val taskSections: List<TaskSection> = emptyList(),
    val taskCount: Int = 0,
    val filter: FilterType = FilterType.ALL,
    val sort: SortType = SortType.CUSTOM,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(FilterType.ALL)

    @OptIn(FlowPreview::class)
    val uiState: StateFlow<HomeUiState> =
        combine(repository.getAllTasks(), _filter, preferenceRepository.getSortType()) { tasks, filter, sort ->
            HomeUiState(
                tasks = tasks.applyFilter(filter).sortedBy { it.position },
                taskSections = tasks.toTaskSections(sort),
                taskCount = tasks.size,
                filter = if (tasks.isNotEmpty()) filter else FilterType.ALL,
                sort = sort,
                isLoading = false,
                error = null
            )
        }
            .catch {
                emit(
                    HomeUiState(
                        isLoading = false,
                        error = it.message
                    )
                )
            }
            .stateIn(
                initialValue = HomeUiState(isLoading = true),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )

    init {
        viewModelScope.launch {
            preferenceRepository.getFilterType().take(1).collect {
                _filter.value = it
            }
        }
    }


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

    fun updateFilter(filter: FilterType) {
        _filter.value = filter
        viewModelScope.launch {
            preferenceRepository.saveFilterType(filter)
        }
    }

    private fun List<Task>.applyFilter(filter: FilterType) = when (filter) {
        FilterType.ALL -> this
        FilterType.COMPLETED -> filter { it.completed }
        FilterType.ACTIVE -> filter { !it.completed }
    }

    fun saveOrder(tasks: List<Task>) {
        viewModelScope.launch {
            repository.updatePositions(tasks)
        }
    }

    fun setSortType(sortType: SortType) {
        viewModelScope.launch {
            preferenceRepository.saveSortType(sortType)
        }
    }
}

