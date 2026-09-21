package com.momentum.app.ui.habits

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momentum.app.domain.model.Habit
import com.momentum.app.domain.model.HabitLog
import com.momentum.app.ui.components.*
import com.momentum.app.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    onNavigateToAddHabit: () -> Unit,
    onNavigateToEditHabit: (Long) -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val today = LocalDate.now()
    val monday = today.with(DayOfWeek.MONDAY)
    var habitToDelete by remember { mutableStateOf<Habit?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Habits", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = onNavigateToAddHabit) {
                        Icon(Icons.Filled.Add, contentDescription = "Add habit", tint = SageGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SageGreen)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp, top = 8.dp)
            ) {
                // Consistency score summary card
                item {
                    MomentumCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "This Week's Consistency",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = OnSurface,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    when {
                                        uiState.overallConsistency >= 80 -> "You're building great momentum!"
                                        uiState.overallConsistency >= 50 -> "Solid progress — keep showing up."
                                        uiState.overallConsistency > 0 -> "Every day you show up counts."
                                        else -> "A new week, a fresh start."
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceSubtle
                                )
                            }
                            Text(
                                "${uiState.overallConsistency}%",
                                style = MaterialTheme.typography.headlineMedium,
                                color = SageGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        MomentumProgressBar(progress = uiState.overallConsistency / 100f)
                    }
                }

                // Empty state
                if (uiState.habitsWithData.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Outlined.Autorenew,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = SageGreen
                                )
                                Text(
                                    "No habits yet.",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = OnSurface
                                )
                                Text(
                                    "Start with one small habit. Consistency beats intensity.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurfaceSubtle
                                )
                                Spacer(Modifier.height(8.dp))
                                Button(
                                    onClick = onNavigateToAddHabit,
                                    colors = ButtonDefaults.buttonColors(containerColor = SageGreen)
                                ) {
                                    Text("Add your first habit")
                                }
                            }
                        }
                    }
                }

                // Habit cards
                items(uiState.habitsWithData, key = { it.habit.id }) { habitData ->
                    MomentumCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    habitData.habit.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = OnSurface,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "${habitData.habit.frequencyPerWeek}× per week · ${habitData.consistencyScore}% this week",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceSubtle
                                )
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = { onNavigateToEditHabit(habitData.habit.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = "Edit habit",
                                        tint = OnSurfaceSubtle,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { habitToDelete = habitData.habit },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Delete habit",
                                        tint = OnSurfaceSubtle,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        // Week grid
                        HabitWeekGrid(
                            weekLogs = habitData.weekLogs,
                            today = today,
                            monday = monday,
                            onDayClick = { day -> viewModel.toggleHabit(habitData.habit, day, habitData.weekLogs[day]) }
                        )
                    }
                }
            }
        }
    }

    // Delete confirmation
    habitToDelete?.let { habit ->
        AlertDialog(
            onDismissRequest = { habitToDelete = null },
            title = { Text("Remove habit?") },
            text = {
                Text(
                    "\"${habit.name}\" and all its history will be removed. " +
                            "You can always start fresh with a new one.",
                    color = OnSurfaceMuted
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteHabit(habit.id)
                        habitToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = SoftError)
                ) { Text("Remove") }
            },
            dismissButton = {
                TextButton(onClick = { habitToDelete = null }) { Text("Keep it") }
            },
            containerColor = SurfaceContainer
        )
    }
}
