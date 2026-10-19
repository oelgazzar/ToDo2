package com.example.todo.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.todo.ui.components.FilterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun getFilterType(): Flow<FilterType> {
        return dataStore.data.map { FilterType.valueOf(it[FILTER_KEY]?: FilterType.ALL.name) }
    }

    suspend fun saveFilterType(filterType: FilterType) {
        dataStore.edit {
            it[FILTER_KEY] = filterType.name
        }
    }

    companion object {
        val FILTER_KEY = stringPreferencesKey("filter")
    }
}