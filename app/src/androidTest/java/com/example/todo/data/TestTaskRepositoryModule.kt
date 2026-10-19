package com.example.todo.data

import com.example.todo.di.TaskRepositoryModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [TaskRepositoryModule::class])
abstract class TestTaskRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsTaskRepository(taskRepositoryImp: FakeTaskRepository): TaskRepository
}