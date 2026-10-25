package com.example.todo.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.test.account_balance_wallet
import com.example.test.favorite
import com.example.test.flight
import com.example.test.home
import com.example.test.person
import com.example.test.school
import com.example.test.shopping_cart
import com.example.test.work

data class Category(
    val id: Long = 0,
    val name: String,
    val color: CategoryColor? = null,
    val icon: CategoryIcon? = null,
    val position: Int = 0
)

enum class CategoryColor(val argb: Long) {
    RED(0xFFE57373),
    ORANGE(0xFFFFB74D),
    AMBER(0xFFFFD54F),
    GREEN(0xFF81C784),
    TEAL(0xFF4DB6AC),
    BLUE(0xFF64B5F6),
    INDIGO(0xFF7986CB),
    PURPLE(0xFFBA68C8),
    PINK(0xFFF06292),
    BROWN(0xFFA1887F),
    GRAY(0xFF90A4AE);
}

enum class CategoryIcon(val icon: ImageVector) {
    Personal(person),
    Work(work),
    Shopping(shopping_cart),
    Health(favorite),
    Finance(account_balance_wallet),
    Home(home),
    Study(school),
    Travel(flight),
}