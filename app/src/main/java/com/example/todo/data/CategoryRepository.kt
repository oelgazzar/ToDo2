package com.example.todo.data

import androidx.room3.withWriteTransaction
import com.example.todo.models.Category
import com.example.todo.models.utils.nextDefaultColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val db: AppDatabase,
    private val categoryDao: CategoryDao
) {
    fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { it.toDomain() }
    }

    suspend fun upsertCategory(category: Category): Long {
        return db.withWriteTransaction {
            val categories = categoryDao.getAllCategories().first().toDomain()
            val entity = if (category.id == 0L) { // New task, assign position at the tail
                val position = categoryDao.getNextPosition()
                category.copy(
                    position = position,
                    color = categories.nextDefaultColor()
                ).toEntity()
            } else category.toEntity()

            categoryDao.upsertCategory(entity)
        }
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.id)
    }
}