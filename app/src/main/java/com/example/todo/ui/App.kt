package com.example.todo.ui

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo.ui.navigation.AppNavHost
import com.example.todo.ui.theme.AppTheme
import com.example.todo.notifications.rememberNotificationPermissionState

@Composable
fun App() {
    val permissionStatus = rememberNotificationPermissionState()

    LaunchedEffect(permissionStatus.status) {
        if (!permissionStatus.isGranted) {
            permissionStatus.requestPermission()
        }
    }

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