package com.momentum.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.momentum.app.ui.*
import com.momentum.app.data.repository.SettingsRepository
import com.momentum.app.ui.balance.BalanceScreen
import com.momentum.app.ui.onboarding.OnboardingScreen
import com.momentum.app.ui.settings.SettingsScreen
import com.momentum.app.ui.habits.AddEditHabitScreen
import com.momentum.app.ui.habits.HabitsScreen
import com.momentum.app.ui.insights.InsightsScreen
import com.momentum.app.ui.reflection.ReflectionScreen
import com.momentum.app.ui.tasks.AddEditTaskScreen
import com.momentum.app.ui.tasks.TasksScreen
import com.momentum.app.ui.theme.*
import com.momentum.app.ui.today.TodayScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import android.content.Intent
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    private val targetRoute = MutableStateFlow<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleIntent(intent)
        setContent {
            MomentumTheme {
                val settings by settingsRepository.getSettings().collectAsState(initial = null)
                val explicitTarget by targetRoute.collectAsState()

                if (settings != null) {
                    val startRoute = when {
                        explicitTarget != null -> explicitTarget!!
                        settings!!.isOnboardingCompleted -> AppRoutes.TODAY
                        else -> AppRoutes.ONBOARDING
                    }
                    MomentumAppContent(startDestination = startRoute)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.getStringExtra("navigate_to") == "reflection") {
            targetRoute.value = AppRoutes.REFLECTION
        }
    }
}

@Composable
fun MomentumAppContent(startDestination: String = AppRoutes.TODAY) {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem.Today,
        BottomNavItem.Tasks,
        BottomNavItem.Habits,
        BottomNavItem.Reflection
    )

    // Only show bottom nav for main tabs
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf(
        AppRoutes.TODAY, AppRoutes.TASKS, AppRoutes.HABITS, AppRoutes.REFLECTION
    )

    Scaffold(
        containerColor = Background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = SurfaceContainer, tonalElevation = 0.dp) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    item.label,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SageGreen,
                                selectedTextColor = SageGreen,
                                indicatorColor = SageGreenContainer,
                                unselectedIconColor = OnSurfaceSubtle,
                                unselectedTextColor = OnSurfaceSubtle
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Today Dashboard
            composable(AppRoutes.TODAY) {
                TodayScreen(
                    onNavigateToAddTask = {
                        navController.navigate(AppRoutes.ADD_EDIT_TASK.replace("{taskId}", "-1"))
                    },
                    onNavigateToInsights = {
                        navController.navigate(AppRoutes.INSIGHTS)
                    },
                    onNavigateToBalance = {
                        navController.navigate(AppRoutes.BALANCE)
                    },
                    onNavigateToSettings = {
                        navController.navigate(AppRoutes.SETTINGS)
                    },
                    onNavigateToReflect = {
                        navController.navigate(AppRoutes.REFLECTION) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            // Tasks
            composable(AppRoutes.TASKS) {
                TasksScreen(
                    onNavigateToAddTask = {
                        navController.navigate(AppRoutes.ADD_EDIT_TASK.replace("{taskId}", "-1"))
                    },
                    onNavigateToEditTask = { taskId ->
                        navController.navigate(AppRoutes.ADD_EDIT_TASK.replace("{taskId}", taskId.toString()))
                    }
                )
            }

            // Add/Edit Task
            composable(
                route = AppRoutes.ADD_EDIT_TASK,
                arguments = listOf(
                    androidx.navigation.navArgument("taskId") {
                        type = androidx.navigation.NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getLong("taskId")?.takeIf { it != -1L }
                AddEditTaskScreen(
                    taskId = taskId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Habits
            composable(AppRoutes.HABITS) {
                HabitsScreen(
                    onNavigateToAddHabit = {
                        navController.navigate(AppRoutes.ADD_EDIT_HABIT.replace("{habitId}", "-1"))
                    },
                    onNavigateToEditHabit = { habitId ->
                        navController.navigate(AppRoutes.ADD_EDIT_HABIT.replace("{habitId}", habitId.toString()))
                    }
                )
            }

            // Add/Edit Habit
            composable(
                route = AppRoutes.ADD_EDIT_HABIT,
                arguments = listOf(
                    androidx.navigation.navArgument("habitId") {
                        type = androidx.navigation.NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) { backStackEntry ->
                val habitId = backStackEntry.arguments?.getLong("habitId")?.takeIf { it != -1L }
                AddEditHabitScreen(
                    habitId = habitId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Daily Reflection
            composable(AppRoutes.REFLECTION) {
                ReflectionScreen()
            }

            // Weekly Insights (no bottom nav)
            composable(AppRoutes.INSIGHTS) {
                InsightsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Life Balance
            composable(AppRoutes.BALANCE) {
                BalanceScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Settings
            composable(AppRoutes.SETTINGS) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Onboarding
            composable(AppRoutes.ONBOARDING) {
                OnboardingScreen(
                    onFinishOnboarding = {
                        navController.navigate(AppRoutes.TODAY) {
                            popUpTo(AppRoutes.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
