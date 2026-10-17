package com.example.todo.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.todo.models.DueBucket

data class DueBucketUi(
    val displayString: String,
    val icon: ImageVector,
    val color: Color
)

fun DueBucket.toUi(): DueBucketUi = when (this) {
    DueBucket.NO_DUE_DATE -> DueBucketUi(
        displayString = "NO DUE DATE",
        color = Color(0xff727584),
        icon = Icons.Filled.NotInterested,
    )
    DueBucket.TODAY -> DueBucketUi(
        displayString = "TODAY",
        icon = Icons.Filled.CalendarToday,
        color = Color(0xff3d70c3)
    )

    DueBucket.OVERDUE -> DueBucketUi(
        displayString = "OVERDUE",
        icon = Icons.Filled.Error,
        color = Color(0xffe95a6c)
    )

    DueBucket.TOMORROW -> DueBucketUi(
        displayString = "TOMORROW",
        icon = Icons.Filled.CalendarMonth,
        color = Color(0xffebaf1b)
    )

    DueBucket.THIS_WEEK -> DueBucketUi(
        displayString = "THIS WEEK",
        icon = Icons.Filled.CalendarMonth,
        color = Color(0xff7447ec)
    )

    DueBucket.NEXT_WEEK -> DueBucketUi(
        displayString = "NEXT WEEK",
        icon = Icons.Filled.CalendarMonth,
        color = Color(0xff246fee)
    )

    else -> DueBucketUi(
        displayString = "UPCOMING",
        icon = Icons.Filled.CalendarMonth,
        color = Color(0xff246fee)
    )
}