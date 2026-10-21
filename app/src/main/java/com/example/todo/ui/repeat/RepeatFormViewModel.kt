package com.example.todo.ui.repeat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.TaskRepository
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
): ViewModel() {
    val taskId: Long = checkNotNull(savedStateHandle["taskId"])

    private val _repeatFormState = MutableStateFlow(RepeatFormState())
    val repeatState = _repeatFormState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTask(taskId).first()?.let { task ->
                _repeatFormState.update {
                    task.repeat?.toRepeatFormState()?: RepeatFormState()
                }
            }
        }
    }

    suspend fun saveRepeat() {
        val repeat = _repeatFormState.value.toRepeat()
        repository.updateRepeat(taskId, repeat)
    }
}