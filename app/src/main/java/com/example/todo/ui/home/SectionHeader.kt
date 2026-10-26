package com.example.todo.ui.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.todo.models.Category
import com.example.todo.models.SortType
import com.example.todo.ui.components.CategoryIcon

fun LazyListScope.sectionHeader(
    title: String,
    selectedSortType: SortType,
    shape: Shape,
    color: Color,
    category: Category? = null,
    modifier: Modifier = Modifier.Companion
) {
    item(key = "title_${title}") {
        Card(
            shape = shape,
            modifier = modifier.padding(top = 1.dp) // gap above section
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                if (selectedSortType == SortType.CATEGORY) {
                    category?.let {
                        CategoryIcon(category)
                    }
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = color,
                )
            }
        }
    }
}