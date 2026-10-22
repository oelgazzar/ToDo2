package com.example.todo.ui.home.quickAdd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TaskRepository
import com.example.todo.models.Reminder
import com.example.todo.ui.repeat.RepeatFormState
import com.example.todo.ui.repeat.toRepeat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class QuickAddViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {
    private val _repeatFormState = MutableStateFlow(RepeatFormState())
    val repeatState = _repeatFormState.asStateFlow()

    private val _taskDraftState = MutableStateFlow(TaskDraftState())
    val taskDraftState = _taskDraftState.asStateFlow()

    private var repeatSubmitted = false

    init {
        println("QuickAddViewModel init")
    }

    fun reset() {
        _taskDraftState.value = TaskDraftState()
        _repeatFormState.value = RepeatFormState()
    }

    fun addTask() {
        viewModelScope.launch {
            repository.upsertTask(_taskDraftState.value.toTask())
        }
    }

    fun onRepeatSubmitted() {
        _taskDraftState.value.apply {
            updateRepeat(repeatState.value.toRepeat())
            updateReminder(Reminder(
                repeatState.value.startDate?: LocalDate.now(),
                repeatState.value.time
            ))
        }
    }

    override fun onCleared() {
        println("QuickAddViewModel onCleared")
    }
}