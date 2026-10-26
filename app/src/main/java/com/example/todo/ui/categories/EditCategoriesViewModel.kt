package com.example.todo.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.CategoryRepository
import com.example.todo.models.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {
    private val _editorState = MutableStateFlow<CategoryEditorState?>(null)
    val editorState = _editorState.asStateFlow()

    val categories = categoryRepository.getAllCategories()
        .stateIn(
            viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun startAdd() {
        _editorState.value = CategoryEditorState()
    }

    fun startEdit(category: Category) {
        _editorState.value = CategoryEditorState.fromCategory(category)
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
        }
    }

    fun onSave() {
        viewModelScope.launch {
            _editorState.value?.let {
                categoryRepository.upsertCategory(it.toCategory())
            }
            _editorState.value = null
        }
    }

    fun onDismiss() {
        println("onDismiss")
        _editorState.value = null
        println(_editorState.value)
    }
}