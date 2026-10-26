package com.example.todo.ui.home

import androidx.annotation.DrawableRes

data class EmptyStateUI(
    @DrawableRes val image: Int,
    val title: String,
    val description: String
)