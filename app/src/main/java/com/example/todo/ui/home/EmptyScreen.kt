package com.example.todo.ui.home

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
import com.example.todo.ui.components.FilterType
import com.example.todo.ui.theme.AppTheme
import com.example.todo.ui.utils.toEmptyStateUI

@Composable
fun EmptyScreen(
    noTasks: Boolean,
    selectedFilter: FilterType,
    modifier: Modifier = Modifier
) {
    val emptyStateUI = selectedFilter.toEmptyStateUI(noTasks)

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
            noTasks = true,
            selectedFilter = FilterType.COMPLETED
        )
    }
}