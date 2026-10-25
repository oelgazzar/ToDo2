package com.example.todo.data

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category WHERE id = :id")
    fun getCategory(id: Long): Flow<CategoryEntity?>

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getCategoryOnce(id: Long): CategoryEntity?

    @Upsert
    suspend fun upsertCategory(category: CategoryEntity): Long

    @Query("UPDATE category SET position = :position WHERE id = :categoryId")
    suspend fun updatePosition(categoryId: Long, position: Int)

    @Query("SELECT COALESCE(MAX(position), -1) + 1 FROM category")
    suspend fun getNextPosition(): Int

    @Query("DELETE FROM category WHERE id = :id")
    suspend fun deleteCategory(id: Long)
}