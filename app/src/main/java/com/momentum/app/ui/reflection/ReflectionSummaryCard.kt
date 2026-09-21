package com.momentum.app.ui.reflection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.momentum.app.domain.model.ReflectionEntry
import com.momentum.app.ui.components.MomentumCard
import com.momentum.app.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun energyLabel(level: Int): String = when (level) {
    1 -> "1 — Drained"
    2 -> "2 — Tired"
    3 -> "3 — Okay"
    4 -> "4 — Energized"
    5 -> "5 — Peak Energy"
    else -> "$level"
}

@Composable
fun ReflectionSummaryCard(
    entry: ReflectionEntry,
    modifier: Modifier = Modifier,
    isToday: Boolean = entry.date == LocalDate.now(),
    onEditClick: (() -> Unit)? = null
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy")
    val dateString = entry.date.format(dateFormatter)

    MomentumCard(modifier = modifier) {
        // Header: Date + Today badge + Optional Edit Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface
                    )
                    if (isToday) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SageGreenContainer)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Today",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SageGreen
                            )
                        }
                    }
                }
            }

            if (onEditClick != null) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit reflection",
                        tint = SageGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Mood and Energy Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceContainerHighest)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mood
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = entry.mood.icon,
                    contentDescription = entry.mood.label,
                    tint = SageGreen,
                    modifier = Modifier.size(28.dp)
                )
                Column {
                    Text(
                        text = "Mood",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceSubtle
                    )
                    Text(
                        text = entry.mood.label,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface
                    )
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .background(SurfaceContainer)
            )

            // Energy Level
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Bolt,
                    contentDescription = "Energy Level",
                    tint = SageGreen,
                    modifier = Modifier.size(28.dp)
                )
                Column {
                    Text(
                        text = "Energy",
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceSubtle
                    )
                    Text(
                        text = energyLabel(entry.energyLevel),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider(color = SurfaceContainerHighest, thickness = 1.dp)
        Spacer(Modifier.height(12.dp))

        // 3 Questions & Answers
        SummaryQuestionItem(
            icon = Icons.Outlined.AutoAwesome,
            question = "What went well today?",
            answer = entry.wentWell
        )

        Spacer(Modifier.height(12.dp))

        SummaryQuestionItem(
            icon = Icons.Outlined.TrackChanges,
            question = "What distracted you most?",
            answer = entry.distracted
        )

        Spacer(Modifier.height(12.dp))

        SummaryQuestionItem(
            icon = Icons.Outlined.Spa,
            question = "What could make tomorrow better?",
            answer = entry.improveTomorrow
        )
    }
}

@Composable
private fun SummaryQuestionItem(
    icon: ImageVector,
    question: String,
    answer: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SageGreen,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = question,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = OnSurface
            )
        }

        val hasAnswer = !answer.isNullOrBlank()
        Text(
            text = if (hasAnswer) answer!!.trim() else "Not answered",
            style = MaterialTheme.typography.bodyMedium,
            color = if (hasAnswer) OnSurface else OnSurfaceSubtle,
            fontStyle = if (hasAnswer) FontStyle.Normal else FontStyle.Italic,
            modifier = Modifier.padding(start = 26.dp)
        )
    }
}
