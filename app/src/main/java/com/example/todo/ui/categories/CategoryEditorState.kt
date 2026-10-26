package com.example.todo.ui.categories

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.todo.models.Category
import com.example.todo.models.CategoryColor
import com.example.todo.models.CategoryIcon

@Stable
class CategoryEditorState {
    private var id: Long = 0
    private var position: Int = 0

    val name = TextFieldState()
    var color by mutableStateOf(CategoryColor.GRAY)
        private set
    var icon by mutableStateOf<CategoryIcon?>(null)
        private set

    fun updateColor(color: CategoryColor) {
        this.color = color
    }

    fun updateIcon(icon: CategoryIcon?) {
        this.icon = icon
    }

    fun toCategory(): Category {
        return Category(
            id = id,
            name = name.text.toString(),
            color = color,
            icon = icon,
            position = position
        )
    }

    companion object {
        fun fromCategory(category: Category): CategoryEditorState {
            return CategoryEditorState().apply {
                id = category.id
                name.setTextAndPlaceCursorAtEnd(text = category.name)
                color = category.color ?: CategoryColor.GRAY
                icon = category.icon
                position = category.position
            }
        }
    }
}