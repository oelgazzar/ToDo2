package com.example.todo.ui.utils

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.example.todo.models.SortType
import com.example.todo.models.TaskSection

fun TaskSection.getHeaderDisplayColor(sortType: SortType, colorScheme: ColorScheme): Color {
    return when(sortType) {
        SortType.DEADLINE -> items.first().schedule.dueDate?.getDisplayColor(colorScheme)?:Color.Unspecified
        SortType.REMINDER -> items.first().schedule.reminder?.getDisplayColor(colorScheme)?:Color.Unspecified
        else -> Color.Unspecified
    }
}