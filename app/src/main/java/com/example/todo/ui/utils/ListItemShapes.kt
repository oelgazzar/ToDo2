package com.example.todo.ui.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

val largeRadius = 24.dp
val smallRadius = 4.dp

val EmptyShape = RoundedCornerShape(largeRadius)
val HeaderShape = RoundedCornerShape(largeRadius, largeRadius, smallRadius, smallRadius)
val FirstItemShape = RoundedCornerShape(smallRadius, smallRadius, 0.dp, 0.dp)
val LastItemShape = RoundedCornerShape(0.dp, 0.dp, smallRadius, smallRadius)
val BottomItemShape = RoundedCornerShape(0.dp, 0.dp, largeRadius, largeRadius)

interface ListShapes {
    val emptyShape: Shape
        get() = RoundedCornerShape(largeRadius)
    val headerShape: Shape
    val firstItemShape: Shape
    val lastItemShape: Shape
    val bottomItemShape: Shape
}

object FlatListShapes: ListShapes {
    override val headerShape: Shape = RoundedCornerShape(largeRadius, largeRadius, 0.dp, 0.dp)
    override val firstItemShape: Shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)
    override val lastItemShape: Shape = RoundedCornerShape(0.dp, 0.dp, largeRadius, largeRadius)
    override val bottomItemShape: Shape = lastItemShape
}

object SectionedListShapes: ListShapes {
    override val headerShape: Shape = RoundedCornerShape(largeRadius, largeRadius, smallRadius, smallRadius)
    override val firstItemShape: Shape = RoundedCornerShape(smallRadius, smallRadius, 0.dp, 0.dp)
    override val lastItemShape: Shape = RoundedCornerShape(0.dp, 0.dp, smallRadius, smallRadius)
    override val bottomItemShape: Shape = RoundedCornerShape(0.dp, 0.dp, largeRadius, largeRadius)
}