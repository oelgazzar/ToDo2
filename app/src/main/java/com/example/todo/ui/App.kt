package com.example.todo.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo.ui.home.HomeScreen
import com.example.todo.ui.navigation.AppNavHost
import com.example.todo.ui.theme.AppTheme

@Composable
fun App() {
    Surface {
        AppNavHost()
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppTheme {
        App()
    }
}