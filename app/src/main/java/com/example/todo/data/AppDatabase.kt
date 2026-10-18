package com.example.todo.data

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(entities = [TaskEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "tasks_db"
    }
}