package com.example.todo.ui.repeat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.test.arrow_back
import com.example.todo.ui.components.DateField
import com.example.todo.ui.components.DayOfWeekPicker
import com.example.todo.ui.components.DropdownSelector
import com.example.todo.ui.components.TimeField
import com.example.todo.ui.theme.AppTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
data class RepeatForm(val taskId: Long)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatScreen(
    navigateBack: () -> Unit,
    viewModel: RepeatFormViewModel = hiltViewModel()
) {
    val repeatState by viewModel.repeatState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Repeat")
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(arrow_back, null)
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.saveRepeat()
                                navigateBack()
                            }
                        }
                    ) {
                        Text("Done")
                    }
                }
            )
        }
    ) { innerPadding ->
        RepeatContent(
            state = repeatState,
            modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatContent(
    state: RepeatFormState,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text("Every")
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.interval,
                onValueChange = { state.updateInterval(it) },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                maxLines = 1,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .widthIn(max = 56.dp)
            )

            DropdownSelector(
                values = RepeatType.entries,
                currentValue = state.repeatType,
                onValueChange = { state.updateRepeatType(it) },
                quantity = state.interval.toIntOrNull() ?: 1,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }

        if (state.repeatType == RepeatType.Week) {
            DayOfWeekPicker(state = state.dayOfWeekPickerState)
        }

        if (state.repeatType == RepeatType.Month) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RadioButton(
                    selected = state.monthDayMode == MonthDayMode.Day,
                    onClick = {
                        state.updateMonthDayMode(MonthDayMode.Day)
                    }
                )
                DropdownSelector(
                    values = (1..31).map { "Day $it" },
                    currentValue = "Day ${state.dayOfMonth}",
                    onValueChange = {
                        val dayOfMonth = it.replace("Day ", "").toInt()
                        state.updateDayOfMonth(dayOfMonth)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                RadioButton(
                    selected = state.monthDayMode == MonthDayMode.Weekday,
                    onClick = { state.updateMonthDayMode(MonthDayMode.Weekday) }
                )
                DropdownSelector(
                    values = listOf("First", "Second", "Third", "Fourth", "Last"),
                    currentValue = when (state.nthDayOfWeekInMonth) {
                        1 -> "First"
                        2 -> "Second"
                        3 -> "Third"
                        4 -> "Fourth"
                        else -> "Last"
                    },
                    onValueChange = {
                        val nthDayOfWeekInMonth = when (it) {
                            "First" -> 1
                            "Second" -> 2
                            "Third" -> 3
                            "Fourth" -> 4
                            else -> -1
                        }
                        state.updateNthDayOfWeekInMonth(nthDayOfWeekInMonth)
                    },
                    modifier = Modifier.weight(1f)
                )
                DropdownSelector(
                    values = DayOfWeek.entries,
                    currentValue = state.dayOfWeekInMonth,
                    onValueChange = { state.updateDayOfWeekInMonth(it) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        TimeField(
            time = state.time,
            onTimeChange = { state.updateTime(it) },
            modifier = Modifier.fillMaxWidth()
        )
        // ============================================================
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        // ============================================================

        Text("Starts")
        DateField(
            date = state.startDate,
            onDateChange = { state.updateStartDate(it) },
            modifier = Modifier.fillMaxWidth()
        )

        // ============================================================
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        // ============================================================

        Text("Ends")
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                state.repeatEndMode == RepeatEndMode.Never,
                onClick = { state.updateRepeatEndMode(RepeatEndMode.Never) }
            )
            Text("Never", modifier = Modifier.widthIn(min = 50.dp))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                state.repeatEndMode == RepeatEndMode.OnDate,
                onClick = { state.updateRepeatEndMode(RepeatEndMode.OnDate) }
            )
            Text("On", modifier = Modifier.widthIn(min = 50.dp))
            DateField(
                state.endDate,
                onDateChange = { state.updateEndDate(it) },
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                state.repeatEndMode == RepeatEndMode.AfterNOccurrences,
                onClick = { state.updateRepeatEndMode(RepeatEndMode.AfterNOccurrences) }
            )
            Text("After", modifier = Modifier.widthIn(min = 50.dp))
            OutlinedTextField(
                value = state.endAfter,
                onValueChange = { state.updateEndAfter(it) },
                readOnly = true,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .widthIn(max = 56.dp)
            )
            Text("Occurrences")
        }
    }
}

@Preview()
@Composable
private fun RepeatContentPrev() {
    AppTheme {
        RepeatContent(
            state = RepeatFormState(),
            modifier = Modifier.padding(16.dp)
        )
    }
}