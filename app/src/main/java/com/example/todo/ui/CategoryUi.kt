package com.example.todo.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.todo.models.Category

data class CategoryUi(
    val icon: ImageVector?,
    val color: Color
)

fun Category.toUi(): CategoryUi = when(this) {
    Category.WORK -> CategoryUi(
        icon = Icons.Filled.Work,
        color = Color(0xff3d70c3)
    )

        Category.STUDY -> CategoryUi(
        icon = Icons.Filled.Book,
        color = Color(0xff5b8158)
    )

    Category.PERSONAL -> CategoryUi(
        icon = Icons.Filled.Person,
        color = Color(0xff246fee)
    )

    Category.SHOPPING -> CategoryUi(
            icon = Icons.Filled.Shop,
            color = Color(0xffebaf1b)
        )

    Category.HEALTH -> CategoryUi(
        icon = Icons.Filled.SportsTennis,
        color = Color(0xff7447ec)
    )

    Category.OTHER -> CategoryUi(
        icon = null,
        color = Color(0xff727584)
    )
}
