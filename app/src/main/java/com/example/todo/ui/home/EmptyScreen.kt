package com.example.todo.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo.R
import com.example.todo.ui.components.FilterType
import com.example.todo.ui.theme.AppTheme

data class EmptyStateUI(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
)

fun FilterType.toEmptyStateUI(totalTaskCount: Int) = when (totalTaskCount) {
    0 -> EmptyStateUI(
        image = R.drawable.no_tasks,
        title = "No tasks available",
        description = "Tap + to add new task"
    )

    else -> when (this) {
        FilterType.ALL -> EmptyStateUI(
            image = R.drawable.no_tasks,
            title = "No tasks available",
            description = "Tap + to add new task"
        )

        FilterType.COMPLETED -> EmptyStateUI(
            image = R.drawable.no_completed,
            title = "No completed tasks.",
            description = "You can do it"
        )

        FilterType.ACTIVE -> EmptyStateUI(
            image = R.drawable.all_completed,
            title = "All tasks complete",
            description = "Nice work!"
        )
    }
}

@Composable
fun EmptyScreen(
    totalTaskCount: Int,
    selectedFilter: FilterType,
    modifier: Modifier = Modifier
) {
    val emptyStateUI = selectedFilter.toEmptyStateUI(totalTaskCount)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
    ) {
        Image(
            painterResource(emptyStateUI.image),
            null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(256.dp)
        )
        Text(
            emptyStateUI.title,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            emptyStateUI.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showSystemUi = true, uiMode = AndroidUiModes.UI_MODE_NIGHT_NO)
@Composable
private fun EmptyHomeContentPrev() {
    AppTheme {
        EmptyScreen(
            totalTaskCount = 0,
            selectedFilter = FilterType.COMPLETED
        )
    }
}