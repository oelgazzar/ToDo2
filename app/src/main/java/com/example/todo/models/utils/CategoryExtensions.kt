package com.example.todo.models.utils

import androidx.compose.ui.graphics.Color
import com.example.todo.models.Category
import com.example.todo.models.CategoryColor

fun List<Category>.nextDefaultColor(): CategoryColor {
    val used = map { it.color }.toSet()
    return CategoryColor.entries.firstOrNull { it !in used }
        ?: CategoryColor.entries.random()
}

fun CategoryColor.toColor() = Color(argb)