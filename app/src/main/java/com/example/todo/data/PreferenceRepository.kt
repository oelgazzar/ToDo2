package com.example.todo.data

import androidx.datastore.preferences.core.edit
import com.example.todo.data.PreferenceRepositoryImpl.Companion.FILTER_KEY
import com.example.todo.ui.components.FilterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PreferenceRepository {
    fun getFilterType(): Flow<FilterType>

    suspend fun saveFilterType(filterType: FilterType)
}