package com.example.todo.ui.navigation

import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.todo.ui.categories.EditCategories
import com.example.todo.ui.categories.EditCategoriesScreen
import com.example.todo.ui.home.Home
import com.example.todo.ui.home.HomeScreen
import com.example.todo.ui.home.quickAdd.QuickAddViewModel
import com.example.todo.ui.repeat.RepeatForm
import com.example.todo.ui.repeat.RepeatFormViewModel
import com.example.todo.ui.repeat.RepeatScreen
import com.example.todo.ui.task.TaskDetails
import com.example.todo.ui.task.TaskScreen

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController,
        Home
    ) {
        composable<Home> {
            HomeScreen(
                navigateToTaskScreen = { navController.navigate(TaskDetails(it)) },
                navigateToRepeatFormScreen = { navController.navigate(RepeatForm(-1, it?.toString())) },
                taskActionViewModel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
            )
        }

        composable<TaskDetails> { backStackEntry ->
            TaskScreen(
                navigateBack = {
                    navController.navigate(Home) {
                        popUpTo<Home> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateToRepeatForm = { navController.navigate(RepeatForm(backStackEntry.toRoute<TaskDetails>().taskId, it?.toString())) },
                navigateToEditCategories = { navController.navigate(EditCategories) },
                taskActionViewModel = hiltViewModel(LocalActivity.current as ViewModelStoreOwner)
            )
        }

        composable<RepeatForm>{
            val previousDestination = navController
                .previousBackStackEntry
                ?.destination

            when {
                previousDestination?.hasRoute<Home>() == true -> {
                    val quickAddViewModel: QuickAddViewModel =
                        hiltViewModel(
                            navController.getBackStackEntry<Home>()
                        )
                    val repeatState by quickAddViewModel.repeatState.collectAsStateWithLifecycle()

                    RepeatScreen(
                        navigateBack = { navController.popBackStack()},
                        repeatState = repeatState,
                        onRepeatSubmitted = quickAddViewModel::onRepeatSubmitted
                    )
                }

                previousDestination?.hasRoute<TaskDetails>() == true -> {
                    val repeatFormViewModel: RepeatFormViewModel = hiltViewModel()

                    val repeatState by repeatFormViewModel.repeatState.collectAsStateWithLifecycle()

                    RepeatScreen(
                        navigateBack = { navController.popBackStack()},
                        repeatState = repeatState,
                        onRepeatSubmitted = repeatFormViewModel::onRepeatSubmitted
                    )
                }
            }
        }

        composable<EditCategories> {
            EditCategoriesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}