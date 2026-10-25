package com.example.todo.data

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import com.example.todo.models.Category
import com.example.todo.models.CategoryColor
import com.example.todo.models.CategoryIcon

@Entity(
    tableName = "category",
    indices = [Index("position", unique = true)]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val color: CategoryColor?,
    val icon: CategoryIcon?,
    val position: Int = 0
)

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    color = color,
    icon = icon,
    position = position
)

fun List<CategoryEntity>.toDomain() = map { it.toDomain() }

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    color = color,
    icon = icon,
    position = position
)

fun List<Category>.toEntity() = map { it.toEntity() }

