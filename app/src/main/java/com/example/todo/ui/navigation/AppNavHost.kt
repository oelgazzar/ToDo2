package com.example.todo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo.ui.home.Home
import com.example.todo.ui.home.HomeScreen
import com.example.todo.ui.repeat.RepeatForm
import com.example.todo.ui.repeat.RepeatScreen
import com.example.todo.ui.task.TaskDetails
import com.example.todo.ui.task.TaskScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController,
        Home
    ) {
        composable<Home> {
            HomeScreen(
                navigateToTaskScreen = { navController.navigate(RepeatForm(it)) }
            )
        }

        composable<TaskDetails> {
            TaskScreen(
                navigateBack = {
                    navController.navigate(Home) {
                        popUpTo<Home> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<RepeatForm> {
            RepeatScreen(
                navigateBack = {
                    navController.navigate(Home) {
                        popUpTo<Home> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}