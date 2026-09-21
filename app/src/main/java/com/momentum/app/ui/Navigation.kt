package com.momentum.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Today : BottomNavItem(
        route = "today",
        label = "Today",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )
    object Tasks : BottomNavItem(
        route = "tasks",
        label = "Tasks",
        selectedIcon = Icons.Filled.CheckCircle,
        unselectedIcon = Icons.Outlined.CheckCircle
    )
    object Habits : BottomNavItem(
        route = "habits",
        label = "Habits",
        selectedIcon = Icons.Filled.Loop,
        unselectedIcon = Icons.Outlined.Loop
    )
    object Reflection : BottomNavItem(
        route = "reflection",
        label = "Reflect",
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome
    )
}

object AppRoutes {
    const val TODAY = "today"
    const val TASKS = "tasks"
    const val ADD_EDIT_TASK = "add_edit_task?taskId={taskId}"
    const val HABITS = "habits"
    const val ADD_EDIT_HABIT = "add_edit_habit?habitId={habitId}"
    const val REFLECTION = "reflection"
    const val INSIGHTS = "insights"
    const val BALANCE = "balance"
    const val SETTINGS = "settings"
    const val ONBOARDING = "onboarding"
}
