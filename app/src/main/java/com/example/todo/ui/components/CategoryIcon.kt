package com.example.todo.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todo.models.Category
import com.example.todo.models.utils.toColor

@Composable
fun CategoryIcon(
    category: Category,
    modifier: Modifier = Modifier.Companion
) {
    category.icon?.let {
        Icon(
            it.icon,
            null,
            tint = category.color?.toColor() ?: Color.Unspecified,
            modifier = modifier
        )
        Spacer(modifier = Modifier.width(8.dp))
    } ?: category.color?.let {
        ColorDot(
            color = it.toColor(),
            modifier = modifier
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}