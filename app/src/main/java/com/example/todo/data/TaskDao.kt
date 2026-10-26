package com.example.todo.data

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTask(id: Long): Flow<TaskEntity?>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun getTaskOnce(id: Long): TaskEntity?

    @Upsert
    suspend fun upsertTask(task: TaskEntity): Long

    @Insert
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTask(id: Long)

    @Query("UPDATE task SET title = :title, details = :details WHERE id = :taskId")
    suspend fun updateTaskContent(taskId: Long, title: String, details: String)

    @Query("UPDATE task SET completed = NOT completed WHERE id = :taskId")
    suspend fun toggleCompleted(taskId: Long)

    @Query("UPDATE task SET completedAt = :now WHERE id = :taskId")
    suspend fun updateCompleted(taskId: Long, now: LocalDate?)

    @Query("UPDATE task SET dueDate = :date WHERE id = :taskId")
    suspend fun updateDueDate(taskId: Long, date: String?)

    @Query("UPDATE task SET position = :position WHERE id = :taskId")
    suspend fun updatePosition(taskId: Long, position: Long)

    @Query("SELECT COALESCE(MAX(position), -1) + 1 FROM task")
    suspend fun getNextPosition(): Long

    @Transaction
    @Query("SELECT * FROM task")
    fun getTasksWithCategory(): Flow<List<TaskWithCategory>>

    @Transaction
    @Query("SELECT * FROM task where id = :taskId")
    fun getTaskWithCategory(taskId: Long): Flow<TaskWithCategory?>

    @Query("UPDATE task SET categoryId = :categoryId WHERE id = :taskId")
    suspend fun updateCategory(taskId: Long, categoryId: Long?)

    @Query("DELETE FROM task WHERE completedAt IS NOT NULL")
    suspend fun deleteAllCompleted()

    @Query("UPDATE task SET starred = NOT starred WHERE id = :id")
    suspend fun toggleStarred(id: Long)

}
