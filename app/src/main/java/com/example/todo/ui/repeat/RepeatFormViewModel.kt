package com.example.todo.ui.repeat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TaskRepository
import com.example.todo.models.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepeatFormViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val taskId: Long = checkNotNull(savedStateHandle["taskId"])
    val reminder: Reminder? =
        savedStateHandle.get<String>("reminder")?.let { Reminder.fromString(it) }

    private val _repeatFormState = MutableStateFlow(RepeatFormState())
    val repeatState = _repeatFormState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTask(taskId).first()?.let { task ->
                _repeatFormState.update {
                    task.schedule.repeat?.toRepeatFormState() ?: RepeatFormState(reminder)
                }
            }
        }
    }

    suspend fun onRepeatSubmitted() {
        val repeat = _repeatFormState.value.toRepeat()
        repository.updateRepeat(taskId, repeat)
    }
}