package com.example.todo.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.models.CategoryColor
import com.example.todo.models.utils.toColor
import com.example.todo.ui.theme.AppTheme

@Composable
fun ColorOption(
    color: CategoryColor,
    selected: Boolean,
    onSelect: (CategoryColor) -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = { onSelect(color) },
                interactionSource = null,
                indication = null
            )
            .then(
                if (selected) Modifier.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    .padding(2.dp)
                else Modifier
            )
            .background(color.toColor(), CircleShape)
            .size(36.dp)
    )
}

@Preview
@Composable
private fun ColorOptionPrev() {
    AppTheme {
        ColorOption(
            color = CategoryColor.RED,
            selected = false,
            onSelect = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun SelectedColorOptionPrev() {
    AppTheme {
        ColorOption(
            color = CategoryColor.RED,
            selected = true,
            onSelect = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}