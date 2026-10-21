package com.example.todo.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek

@Composable
fun DayOfWeekPicker(
    state: DayOfWeekPickerState = rememberDayOfWeekPickerState(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            4.dp,
            Alignment.CenterHorizontally
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        DayOfWeek.entries.forEach {
            OutlinedIconToggleButton(
                checked = it in state.selectedDays,
                onCheckedChange = { checked ->
                    state.toggleDay(it)
                },
                colors = IconButtonDefaults.iconToggleButtonColors(
                    checkedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    checkedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(it.name.substring(0..0).uppercase())
            }
        }
    }
}