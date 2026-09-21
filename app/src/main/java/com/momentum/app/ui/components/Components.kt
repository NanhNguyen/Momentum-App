package com.momentum.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momentum.app.domain.model.*
import com.momentum.app.ui.theme.*
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

// ─────────────── Momentum Card ───────────────

@Composable
fun MomentumCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, SurfaceContainerHighest)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

// ─────────────── Progress Bar ───────────────

@Composable
fun MomentumProgressBar(
    progress: Float,  // 0f to 1f
    modifier: Modifier = Modifier,
    trackColor: Color = StatePending,
    fillColor: Color = SageGreen
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600, easing = EaseOut),
        label = "progress"
    )
    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(50)),
        color = fillColor,
        trackColor = trackColor
    )
}

// ─────────────── Habit Day Cell ───────────────

@Composable
fun HabitDayCell(
    day: DayOfWeek,
    log: HabitLog?,
    isFuture: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dayLabel = day.getDisplayName(TextStyle.NARROW, Locale.getDefault())
    val (bgColor, icon, iconTint) = when {
        isFuture -> Triple(StatePending, null, Color.Transparent)
        log?.status == HabitStatus.DONE -> Triple(SageGreenContainer, Icons.Filled.Check, SageGreen)
        log?.status == HabitStatus.MISSED -> Triple(SurfaceContainerHighest, Icons.Filled.Remove, StateMissed)
        else -> Triple(StatePending, null, Color.Transparent)
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = dayLabel,
            style = MaterialTheme.typography.labelSmall,
            color = OnSurfaceSubtle
        )
        Box(
            modifier = Modifier
                .sizeIn(minWidth = 44.dp, minHeight = 44.dp)
                .clickable(enabled = !isFuture) { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = log?.status?.name ?: "Habit status",
                        modifier = Modifier.size(16.dp),
                        tint = iconTint
                    )
                }
            }
        }
    }
}

// ─────────────── Week Grid ───────────────

@Composable
fun HabitWeekGrid(
    weekLogs: Map<DayOfWeek, HabitLog?>,
    today: java.time.LocalDate,
    monday: java.time.LocalDate,
    onDayClick: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DayOfWeek.entries.forEach { day ->
            val date = monday.with(day)
            val isFuture = date.isAfter(today)
            HabitDayCell(
                day = day,
                log = weekLogs[day],
                isFuture = isFuture,
                onClick = { onDayClick(day) }
            )
        }
    }
}

// ─────────────── Mood Picker ───────────────

@Composable
fun MoodPicker(
    selectedMood: Mood?,
    onMoodSelect: (Mood) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Mood.entries.forEach { mood ->
            val isSelected = mood == selectedMood
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) SageGreenContainer else Color.Transparent)
                    .border(
                        width = if (isSelected) 1.dp else 0.dp,
                        color = if (isSelected) SageGreen else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { onMoodSelect(mood) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = mood.icon,
                        contentDescription = mood.label,
                        modifier = Modifier.size(32.dp),
                        tint = if (isSelected) SageGreen else OnSurfaceSubtle
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = mood.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) SageGreen else OnSurfaceSubtle
                    )
                }
            }
        }
    }
}

// ─────────────── Priority Badge ───────────────

@Composable
fun PriorityDot(priority: Int, modifier: Modifier = Modifier) {
    val color = when (priority) {
        2 -> PriorityHigh
        1 -> PriorityMedium
        else -> PriorityLow
    }
    Box(
        modifier = modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color)
    )
}

// ─────────────── Big 3 Badge ───────────────

@Composable
fun BigThreeBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = SageGreenContainer
    ) {
        Text(
            text = "Big 3",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = SageGreen,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ─────────────── Task Item Row ───────────────

@Composable
fun TaskItemRow(
    task: com.momentum.app.domain.model.Task,
    onToggleComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Circle checkbox with 48dp accessible touch target
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable { onToggleComplete() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        if (task.isCompleted) SageGreen else SteelBlueDark,
                        CircleShape
                    )
                    .background(if (task.isCompleted) SageGreenContainer else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "Completed",
                        modifier = Modifier.size(14.dp),
                        tint = SageGreen
                    )
                }
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (task.isCompleted) OnSurfaceSubtle else OnSurface,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                maxLines = 2
            )
            task.description?.let { desc ->
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceSubtle,
                    maxLines = 1
                )
            }
        }
        if (task.isRecurring) {
            Icon(
                imageVector = Icons.Filled.Repeat,
                contentDescription = "Recurring task",
                modifier = Modifier.size(14.dp),
                tint = OnSurfaceSubtle
            )
        }
        PriorityDot(task.priority)
        if (task.isBigThree) BigThreeBadge()
    }
}

// ─────────────── Section Header ───────────────

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = OnSurface,
            fontWeight = FontWeight.SemiBold
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceMuted
            )
        }
    }
}

// ─────────────── Insight Card ───────────────

@Composable
fun InsightCard(
    text: String,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector = androidx.compose.material.icons.Icons.Outlined.AutoAwesome
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainer)
            .border(1.dp, SurfaceContainerHighest, RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sage green left accent border
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(48.dp)
                .background(SageGreen)
        )
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SageGreen,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(20.dp)
        )
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurface
        )
    }
}
