package com.example.todo.ui.home

import com.example.todo.models.SortType
import com.example.todo.models.TaskSection
import com.example.todo.ui.components.FilterType

data class HomeUiState(
    val taskSections: List<TaskSection> = emptyList(),
    val noTasks: Boolean = true,
    val filter: FilterType = FilterType.ALL,
    val sort: SortType = SortType.CUSTOM,
    val isLoading: Boolean = true,
    val error: String? = null
)