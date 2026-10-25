package com.example.todo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColorDot(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 12.dp
) {
    Box(
        modifier
            .size(size)
            .background(color, CircleShape)
    )
}