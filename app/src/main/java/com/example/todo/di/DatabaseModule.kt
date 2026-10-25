package com.example.todo.di

import android.content.Context
import androidx.room3.Room
import com.example.todo.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder<AppDatabase>(
        context.applicationContext,
        AppDatabase.DATABASE_NAME
    )
        .fallbackToDestructiveMigration(true)
        .build()

    @Singleton
    @Provides
    fun providesTaskDao(database: AppDatabase) = database.taskDao()

    @Singleton
    @Provides
    fun providesCategoryDao(database: AppDatabase) = database.categoryDao()
}