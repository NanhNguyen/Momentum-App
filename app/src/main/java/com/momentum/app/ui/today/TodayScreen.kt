package com.momentum.app.ui.today

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momentum.app.domain.model.Habit
import com.momentum.app.domain.model.HabitLog
import com.momentum.app.domain.model.HabitStatus
import com.momentum.app.ui.components.*
import com.momentum.app.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    // Deterministic daily tagline rotation (consistent throughout the day)
    val taglines = listOf(
        "Small steps, every day. That's momentum.",
        "Progress over perfection, one day at a time.",
        "Consistency isn't never failing; it's always returning.",
        "Focus on what matters most today, gently release the rest.",
        "Momentum is built quietly, in the choices no one sees."
    )
    val dailyTagline = taglines[today.dayOfYear % taglines.size]

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
                                imageVector = Icons.Outlined.TrackChanges,
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
                            TodayHabitCheckItem(
                                habit = habit,
                                log = log,
                                onToggle = { viewModel.toggleHabitForToday(habit, log) }
                            )
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

            // Life Balance Card (with interactive press feedback)
            item {
                val balanceInteraction = remember { MutableInteractionSource() }
                val balancePressed by balanceInteraction.collectIsPressedAsState()
                val balanceScale by animateFloatAsState(
                    targetValue = if (balancePressed) 0.97f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "balance_press_scale"
                )

                MomentumCard(
                    modifier = Modifier
                        .scale(balanceScale)
                        .clickable(
                            interactionSource = balanceInteraction,
                            indication = ripple(),
                            onClick = onNavigateToBalance
                        )
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

            // This Week at a Glance Section (Compact 3-chip overview)
            item {
                MomentumCard {
                    SectionHeader(
                        title = "This Week at a Glance",
                        subtitle = "Weekly progress across habits & tasks"
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WeeklyStatChip(
                            modifier = Modifier.weight(1f),
                            number = "${uiState.weekConsistencyScore}%",
                            label = "Habits",
                            icon = Icons.Outlined.Loop,
                            accentColor = SageGreen
                        )
                        WeeklyStatChip(
                            modifier = Modifier.weight(1f),
                            number = "${uiState.weekTasksCompleted}",
                            label = "Tasks Done",
                            icon = Icons.Outlined.CheckCircle,
                            accentColor = SteelBlue
                        )
                        WeeklyStatChip(
                            modifier = Modifier.weight(1f),
                            number = "${uiState.weekBigThreeRate}%",
                            label = "Big 3 Rate",
                            icon = Icons.Outlined.TrackChanges,
                            accentColor = WarmSand
                        )
                    }
                }
            }

            // Encouragement footer with deterministic rotating tagline
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = SageGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = dailyTagline,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceSubtle
                    )
                }
            }
        }
    }
}

/**
 * Habit check-in row with smooth completion scale-up-then-settle animation,
 * text color transition, and haptic feedback.
 */
@Composable
private fun TodayHabitCheckItem(
    habit: Habit,
    log: HabitLog?,
    onToggle: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isDone = log?.status == HabitStatus.DONE
    val checkboxScale = remember { Animatable(1f) }

    LaunchedEffect(isDone) {
        if (isDone) {
            checkboxScale.animateTo(1.22f, tween(100))
            checkboxScale.animateTo(1f, spring(stiffness = Spring.StiffnessMediumLow))
        }
    }

    val textColor by animateColorAsState(
        targetValue = if (isDone) OnSurfaceSubtle else OnSurface,
        animationSpec = tween(200),
        label = "habit_text_color"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple()
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onToggle()
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .scale(checkboxScale.value)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        if (isDone) SageGreen else SteelBlueDark,
                        CircleShape
                    )
                    .background(if (isDone) SageGreenContainer else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "Done",
                        modifier = Modifier.size(14.dp),
                        tint = SageGreen
                    )
                }
            }
        }

        Text(
            text = habit.name,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            textDecoration = if (isDone) TextDecoration.LineThrough else null,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Compact stat chip for "This Week at a Glance".
 */
@Composable
private fun WeeklyStatChip(
    number: String,
    label: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerHighest)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = number,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceSubtle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
