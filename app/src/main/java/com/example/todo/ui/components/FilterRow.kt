package com.example.todo.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.test.check
import com.example.todo.ui.theme.AppTheme


@Composable
fun FilterRow(
    selectedFilter : FilterType = FilterType.ALL,
    setSelectedFilterType: (FilterType) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        FilterType.entries.forEach {
            FilterChip(
                selected = it == selectedFilter,
                onClick = { setSelectedFilterType(it) },
                label = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                    if (it == selectedFilter) {
                        Icon(check, null)
                    }

                    Text(it.name, textAlign = TextAlign.Center)
                    }
                },
                leadingIcon = {
                },
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
private fun FilterRowPrev() {
    AppTheme{
        FilterRow(
            setSelectedFilterType = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}