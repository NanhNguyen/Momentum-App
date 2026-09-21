package com.momentum.app.ui.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momentum.app.domain.model.Mood
import com.momentum.app.ui.components.*
import com.momentum.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    onNavigateBack: () -> Unit,
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.selectedTab == InsightsTab.WEEKLY) "Weekly Review" else "Monthly Review",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Text(
                        if (uiState.selectedTab == InsightsTab.WEEKLY) uiState.weekLabel else uiState.monthlyState.monthLabel,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceMuted,
                        modifier = Modifier.padding(end = 16.dp)
                    )
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
                contentPadding = PaddingValues(bottom = 32.dp, top = 4.dp)
            ) {
                // Tab Selector (Weekly | Monthly)
                item {
                    TabSwitcher(
                        selectedTab = uiState.selectedTab,
                        onTabSelected = viewModel::selectTab
                    )
                }

                if (uiState.selectedTab == InsightsTab.WEEKLY) {
                    // ── Weekly Review Content ──
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatCard(
                                label = "Tasks Done",
                                value = "${uiState.tasksCompleted}/${uiState.tasksTotal}",
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Habit Score",
                                value = "${uiState.habitConsistencyPercent}%",
                                valueColor = SageGreen,
                                modifier = Modifier.weight(1f)
                            )
                            MoodStatCard(
                                label = "Avg Mood",
                                mood = uiState.averageMood,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Big 3 completion
                    item {
                        MomentumCard {
                            SectionHeader(
                                title = "Big 3 Completion",
                                subtitle = "${uiState.bigThreeDaysCompleted} of 7 days you completed all your Big 3"
                            )
                            Spacer(Modifier.height(10.dp))
                            MomentumProgressBar(
                                progress = uiState.bigThreeDaysCompleted / 7f
                            )
                        }
                    }

                    // Insights section
                    if (uiState.insights.isNotEmpty()) {
                        item {
                            Text(
                                "This Week's Insights",
                                style = MaterialTheme.typography.titleMedium,
                                color = OnSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        items(uiState.insights) { insight ->
                            InsightCard(text = insight)
                        }
                    }

                    // No data placeholder
                    if (uiState.tasksTotal == 0 && uiState.habitConsistencyPercent == 0) {
                        item {
                            MomentumCard {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.BarChart,
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp),
                                        tint = SageGreen
                                    )
                                    Text(
                                        "Not enough data yet",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = OnSurface
                                    )
                                    Text(
                                        "Keep logging your tasks and habits — your weekly story will appear here.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = OnSurfaceSubtle
                                    )
                                }
                            }
                        }
                    }

                    // Footer encouragement
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Spa,
                                contentDescription = null,
                                tint = SageGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                "Remember: progress isn't always linear, and that's perfectly okay.",
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceSubtle
                            )
                        }
                    }
                } else {
                    // ── Monthly Review Content ──
                    val monthly = uiState.monthlyState

                    // Monthly Summary Stats
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatCard(
                                label = "Tasks (30d)",
                                value = "${monthly.tasksCompletedMonth}/${monthly.tasksTotalMonth}",
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Habit Avg",
                                value = "${monthly.habitConsistencyMonth}%",
                                valueColor = SageGreen,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = "Reflections",
                                value = "${monthly.totalReflectionsCount} days",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // 4-Week Consistency Trend Card
                    item {
                        MomentumCard {
                            SectionHeader(
                                title = "4-Week Consistency Trend",
                                subtitle = "Habit discipline over the past month"
                            )

                            Spacer(Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                monthly.weeklyConsistencyTrend.forEach { week ->
                                    val barHeight = (week.scorePercent.coerceIn(5, 100) * 0.9f).dp
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom,
                                        modifier = Modifier.fillMaxHeight()
                                    ) {
                                        Text(
                                            text = "${week.scorePercent}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SageGreen,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(28.dp)
                                                .height(barHeight)
                                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                                .background(SageGreen)
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = week.label,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = OnSurfaceSubtle
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Monthly Mood Distribution Card
                    item {
                        MomentumCard {
                            SectionHeader(
                                title = "Monthly Mood Distribution",
                                subtitle = "How you felt during your reflections"
                            )

                            Spacer(Modifier.height(14.dp))

                            // Stacked horizontal bar
                            if (monthly.totalReflectionsCount > 0) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(SurfaceContainerHighest)
                                ) {
                                    monthly.moodDistribution.forEach { item ->
                                        if (item.percentage > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .weight(item.percentage.toFloat())
                                                    .fillMaxHeight()
                                                    .background(getMoodColor(item.mood))
                                            )
                                        }
                                    }
                                }

                                Spacer(Modifier.height(14.dp))

                                // Grid / rows of mood counts
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    monthly.moodDistribution.forEach { item ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(10.dp)
                                                        .clip(CircleShape)
                                                        .background(getMoodColor(item.mood))
                                                )
                                                Text(
                                                    text = item.mood.label,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = OnSurface
                                                )
                                            }
                                            Text(
                                                text = "${item.count} days (${item.percentage}%)",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = OnSurfaceSubtle
                                            )
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = "No reflections recorded this month yet.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceSubtle
                                )
                            }
                        }
                    }

                    // Real Correlation Insight Card
                    item {
                        MomentumCard {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.AutoAwesome,
                                    contentDescription = null,
                                    tint = SageGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "Monthly Correlation",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = monthly.correlationInsight,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (monthly.hasEnoughDataForCorrelation) OnSurface else OnSurfaceSubtle,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    // Footer encouragement
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Spa,
                                contentDescription = null,
                                tint = SageGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                "Consistency compounds quietly over months and years.",
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceSubtle
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabSwitcher(
    selectedTab: InsightsTab,
    onTabSelected: (InsightsTab) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfaceContainerHigh,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            InsightsTab.entries.forEach { tab ->
                val isSelected = tab == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) SageGreenContainer else Color.Transparent)
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (tab == InsightsTab.WEEKLY) "Weekly Review" else "Monthly Review",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) SageGreen else OnSurfaceSubtle
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = OnSurface
) {
    MomentumCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceSubtle
            )
        }
    }
}

@Composable
private fun MoodStatCard(
    label: String,
    mood: Mood?,
    modifier: Modifier = Modifier
) {
    MomentumCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (mood != null) {
                Icon(
                    imageVector = mood.icon,
                    contentDescription = mood.label,
                    modifier = Modifier.size(28.dp),
                    tint = SageGreen
                )
            } else {
                Text(
                    text = "—",
                    style = MaterialTheme.typography.titleLarge,
                    color = OnSurfaceSubtle
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceSubtle
            )
        }
    }
}

@Composable
private fun InsightCard(text: String, modifier: Modifier = Modifier) {
    MomentumCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = SageGreen,
                modifier = Modifier
                    .size(18.dp)
                    .padding(top = 2.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurface,
                lineHeight = 20.sp
            )
        }
    }
}

private fun getMoodColor(mood: Mood): Color {
    return when (mood) {
        Mood.GREAT -> MoodGreat
        Mood.GOOD -> MoodGood
        Mood.NEUTRAL -> MoodNeutral
        Mood.DIFFICULT -> MoodDifficult
    }
}
