package com.momentum.app.ui.today

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momentum.app.domain.model.HabitStatus
import com.momentum.app.domain.model.Task
import com.momentum.app.ui.components.*
import com.momentum.app.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    onNavigateToAddTask: () -> Unit,
    onNavigateToInsights: () -> Unit,
    onNavigateToBalance: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: TodayViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val today = LocalDate.now()
    val dateStr = today.format(DateTimeFormatter.ofPattern("EEE, MMM d"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = onNavigateToInsights) {
                        Icon(
                            Icons.Filled.BarChart,
                            contentDescription = "Weekly Review",
                            tint = SageGreen
                        )
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = OnSurfaceMuted
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                )
            )
        },
        containerColor = Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = uiState.greeting,
                        style = MaterialTheme.typography.headlineMedium,
                        color = OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceMuted
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "What matters most today?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceSubtle
                    )
                }
            }

            // Big 3 Section
            item {
                MomentumCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionHeader(
                            title = "Your Big 3",
                            subtitle = "${uiState.bigThreeCompleted} of ${uiState.bigThreeTotal} completed"
                        )
                        if (uiState.bigThreeTotal < 3) {
                            IconButton(
                                onClick = onNavigateToAddTask,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Add Big 3 task",
                                    tint = SageGreen
                                )
                            }
                        }
                    }

                    if (uiState.bigThreeTotal > 0) {
                        Spacer(Modifier.height(8.dp))
                        MomentumProgressBar(
                            progress = if (uiState.bigThreeTotal > 0)
                                uiState.bigThreeCompleted.toFloat() / uiState.bigThreeTotal
                            else 0f
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    if (uiState.bigThree.isEmpty()) {
                        Spacer(Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Outlined.TrackChanges,
                                contentDescription = null,
                                tint = SageGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Choose up to 3 things that would make today a success.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = OnSurfaceSubtle
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onNavigateToAddTask,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SageGreen),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SageGreen)
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Add your first Big 3 task")
                        }
                    } else {
                        uiState.bigThree.forEach { task ->
                            TaskItemRow(
                                task = task,
                                onToggleComplete = { viewModel.toggleTaskComplete(task) }
                            )
                            if (task != uiState.bigThree.last()) {
                                HorizontalDivider(
                                    color = SurfaceContainerHighest,
                                    thickness = 0.5.dp
                                )
                            }
                        }
                    }
                }
            }

            // Habits Section
            if (uiState.todayHabits.isNotEmpty()) {
                item {
                    MomentumCard {
                        SectionHeader(
                            title = "Habits Today",
                            subtitle = "Tap to mark as done"
                        )
                        Spacer(Modifier.height(12.dp))
                        uiState.todayHabits.forEachIndexed { index, (habit, log) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleHabitForToday(habit, log) }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Habit status indicator
                                val statusColor = when (log?.status) {
                                    HabitStatus.DONE -> SageGreen
                                    HabitStatus.MISSED -> StateMissed
                                    else -> SurfaceContainerHighest
                                }
                                Checkbox(
                                    checked = log?.status == HabitStatus.DONE,
                                    onCheckedChange = null,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = SageGreen,
                                        uncheckedColor = SteelBlue,
                                        checkmarkColor = Background
                                    )
                                )
                                Text(
                                    text = habit.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (log?.status == HabitStatus.DONE) OnSurfaceSubtle else OnSurface
                                )
                            }
                            if (index < uiState.todayHabits.size - 1) {
                                HorizontalDivider(
                                    color = SurfaceContainerHighest,
                                    thickness = 0.5.dp
                                )
                            }
                        }
                    }
                }
            }

            // Other tasks (non-Big 3) if any
            if (uiState.otherTasks.isNotEmpty()) {
                item {
                    MomentumCard {
                        SectionHeader(title = "Other Tasks Today")
                        Spacer(Modifier.height(8.dp))
                        uiState.otherTasks.forEach { task ->
                            TaskItemRow(
                                task = task,
                                onToggleComplete = { viewModel.toggleTaskComplete(task) }
                            )
                        }
                    }
                }
            }

            // Life Balance Card
            item {
                MomentumCard(
                    modifier = Modifier.clickable { onNavigateToBalance() }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(WarmSand.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Spa,
                                    contentDescription = null,
                                    tint = WarmSand,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Life Balance",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface
                                )
                                val balanceSummary = when {
                                    uiState.entertainmentMinutesToday > 0 && uiState.restMinutesToday > 0 ->
                                        "${uiState.entertainmentMinutesToday}m play • ${uiState.restMinutesToday}m rest logged today"
                                    uiState.entertainmentMinutesToday > 0 ->
                                        "${uiState.entertainmentMinutesToday}m intentional entertainment today"
                                    uiState.restMinutesToday > 0 ->
                                        "${uiState.restMinutesToday}m intentional rest today"
                                    else ->
                                        "Track intentional leisure & rest"
                                }
                                Text(
                                    text = balanceSummary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceSubtle
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = "View Life Balance",
                            tint = SageGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Encouragement footer
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = SageGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Small steps, every day. That's momentum.",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceSubtle
                    )
                }
            }
        }
    }
}
