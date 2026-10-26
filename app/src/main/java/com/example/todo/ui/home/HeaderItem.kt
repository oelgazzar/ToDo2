package com.example.todo.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Card
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.todo.models.SortType
import com.example.todo.ui.components.FilterRow
import com.example.todo.ui.components.FilterType

fun LazyListScope.headerItem(
    selectedFilter: FilterType,
    onSelectFilter: (FilterType) -> Unit,

    selectedSortType: SortType,
    onSelectSortType: (SortType) -> Unit,

    onDeleteAllCompletedTasks: () -> Unit,

    shape: Shape,
    modifier: Modifier = Modifier.Companion
) {
    item(key = "header") {
        Card(
            shape = shape,
            modifier = modifier
        ) {
            TaskListHeader(
                selectedSortType,
                onSelectSortType,
                onDeleteAllCompleted = onDeleteAllCompletedTasks
            )
            FilterRow(
                selectedFilter = selectedFilter,
                setSelectedFilterType = onSelectFilter,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            )
        }
    }
}