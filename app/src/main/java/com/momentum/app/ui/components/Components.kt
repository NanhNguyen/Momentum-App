package com.momentum.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
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
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cellScale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "cell_press_scale"
    )

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
                .scale(cellScale)
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(bounded = false, radius = 22.dp),
                    enabled = !isFuture
                ) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            val isDone = !isFuture && log?.status == HabitStatus.DONE
            val isMissed = !isFuture && log?.status == HabitStatus.MISSED

            when {
                isDone -> {
                    // Done: filled with sage tone + small checkmark icon
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SageGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Done",
                            modifier = Modifier.size(16.dp),
                            tint = Background
                        )
                    }
                }
                isMissed -> {
                    // Missed: neutral gray outline with a thin diagonal line through it (not filled)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, StateMissed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(14.dp)) {
                            drawLine(
                                color = StateMissed,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, 0f),
                                strokeWidth = 1.5.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
                else -> {
                    // Pending / not-yet-due: muted dark fill, no icon
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isFuture) StatePending else SurfaceContainerHighest)
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
            val (moodAccent, moodContainer) = when (mood) {
                Mood.DIFFICULT -> MoodDifficult to MoodDifficultContainer
                Mood.NEUTRAL -> MoodNeutral to MoodNeutralContainer
                Mood.GOOD -> MoodGood to MoodGoodContainer
                Mood.GREAT -> MoodGreat to MoodGreatContainer
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) moodContainer else Color.Transparent)
                    .border(
                        width = if (isSelected) 1.dp else 0.dp,
                        color = if (isSelected) moodAccent else Color.Transparent,
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
                        tint = if (isSelected) moodAccent else OnSurfaceSubtle
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = mood.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) moodAccent else OnSurfaceSubtle
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
    val haptic = LocalHapticFeedback.current
    val checkboxScale = remember { Animatable(1f) }

    LaunchedEffect(task.isCompleted) {
        if (task.isCompleted) {
            checkboxScale.animateTo(1.22f, tween(100))
            checkboxScale.animateTo(1f, spring(stiffness = Spring.StiffnessMediumLow))
        }
    }

    val textColor by animateColorAsState(
        targetValue = if (task.isCompleted) OnSurfaceSubtle else OnSurface,
        animationSpec = tween(200),
        label = "task_text_color"
    )

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
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = false, radius = 24.dp)
                ) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onToggleComplete()
                },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .scale(checkboxScale.value)
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
                color = textColor,
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

// ─────────────── Circular Consistency Ring ───────────────

@Composable
fun CircularConsistencyRing(
    percentage: Int,
    modifier: Modifier = Modifier,
    size: Dp = 68.dp,
    strokeWidth: Dp = 6.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (percentage / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "consistency_ring"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val radius = (this.size.minDimension - strokePx) / 2
            val centerOffset = center

            // Background track
            drawCircle(
                color = SurfaceContainerHighest,
                radius = radius,
                center = centerOffset,
                style = Stroke(width = strokePx)
            )

            // Animated progress arc
            drawArc(
                color = SageGreen,
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(
                    width = strokePx,
                    cap = StrokeCap.Round
                )
            )
        }

        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = SageGreen
        )
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
