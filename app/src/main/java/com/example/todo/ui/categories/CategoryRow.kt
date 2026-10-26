package com.example.todo.ui.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.drag_indicator
import com.example.test.more_vert
import com.example.todo.models.Category
import com.example.todo.models.CategoryColor
import com.example.todo.models.CategoryIcon
import com.example.todo.models.utils.toColor
import com.example.todo.ui.components.ColorDot
import com.example.todo.ui.theme.AppTheme

@Composable
fun CategoryRow(
    category: Category,
    onEdit: (Category) -> Unit,
    onDelete: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    val defaultColor = CategoryColor.GRAY.toColor()
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            drag_indicator,
            null
        )
        category.icon?.let {
            Icon(
                category.icon.icon, null,
                tint = category.color?.toColor() ?: defaultColor
            )
        } ?: ColorDot(
            category.color?.toColor() ?: defaultColor
        )

        Text(category.name)
        Spacer(modifier = Modifier.weight(1f))
        Box {
            IconButton(
                onClick = {
                    menuExpanded = true
                }
            ) {
                Icon(more_vert, null)
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        onEdit(category)
                        menuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        onDelete(category)
                        menuExpanded = false
                    }
                )
            }

        }

    }
}

@Preview
@Composable
private fun CategoryRowPrev() {
    AppTheme {
        CategoryRow(
            Category(
                id = 1,
                name = "Work",
                color = CategoryColor.RED,
                icon = CategoryIcon.Work,
                position = 1
            ),
            onEdit = {},
            onDelete = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        )
    }
}