package com.example.todo.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateSet
import java.time.DayOfWeek

@Composable
fun rememberDayOfWeekPickerState(): DayOfWeekPickerState {
    return remember {
        DayOfWeekPickerState()
    }
}

class DayOfWeekPickerState() {
    val selectedDays: SnapshotStateSet<DayOfWeek> = mutableStateSetOf()

    fun toggleDay(day: DayOfWeek) {
        if (day in selectedDays) {
            selectedDays.remove(day)
        } else {
            selectedDays.add(day)
        }
    }

}