package com.example.todo.di

import com.example.todo.data.PreferenceRepository
import com.example.todo.data.PreferenceRepositoryImpl
import com.example.todo.data.TaskRepository
import com.example.todo.data.TaskRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class PreferencesRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsPrefRepository(prefRepositoryImpl: PreferenceRepositoryImpl): PreferenceRepository
}