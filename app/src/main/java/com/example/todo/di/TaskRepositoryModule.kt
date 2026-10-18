package com.example.todo.di

import com.example.todo.data.TaskRepository
import com.example.todo.data.TaskRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class TaskRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsTaskRepository(taskRepositoryImp: TaskRepositoryImp): TaskRepository
}