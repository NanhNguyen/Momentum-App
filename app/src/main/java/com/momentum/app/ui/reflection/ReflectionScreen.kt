package com.momentum.app.ui.reflection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momentum.app.domain.model.Mood
import com.momentum.app.ui.components.*
import com.momentum.app.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReflectionScreen(
    viewModel: ReflectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            snackbarHostState.showSnackbar("Reflection saved. Well done for taking a moment to reflect.")
            viewModel.resetSaved()
        }
    }

    val dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMM d"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Daily Reflection", fontWeight = FontWeight.Bold)
                },
                actions = {
                    Text(
                        dateStr,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceMuted,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            if (uiState.isEditingExisting) {
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Outlined.EditNote,
                        contentDescription = null,
                        tint = SageGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        "You've already reflected today — feel free to update it.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SageGreen
                    )
                }
            } else {
                Text(
                    "Take a moment to look back. No judgment, just awareness.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceSubtle
                )
            }

            // Mood picker
            MomentumCard {
                Text(
                    "How was your day?",
                    style = MaterialTheme.typography.titleSmall,
                    color = OnSurface,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(12.dp))
                MoodPicker(
                    selectedMood = uiState.mood,
                    onMoodSelect = viewModel::onMoodSelect
                )
            }

            // Energy level
            MomentumCard {
                Text(
                    "Energy Level",
                    style = MaterialTheme.typography.titleSmall,
                    color = OnSurface,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(8.dp))
                Slider(
                    value = uiState.energyLevel.toFloat(),
                    onValueChange = { viewModel.onEnergyChange(it.toInt()) },
                    valueRange = 1f..5f,
                    steps = 3,
                    colors = SliderDefaults.colors(
                        thumbColor = SageGreen,
                        activeTrackColor = SageGreen,
                        inactiveTrackColor = StatePending
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Low", style = MaterialTheme.typography.labelSmall, color = OnSurfaceSubtle)
                    Text(
                        energyLabel(uiState.energyLevel),
                        style = MaterialTheme.typography.labelSmall,
                        color = SageGreen
                    )
                    Text("High", style = MaterialTheme.typography.labelSmall, color = OnSurfaceSubtle)
                }
            }

            // Reflection questions
            ReflectionCard(
                icon = androidx.compose.material.icons.Icons.Outlined.AutoAwesome,
                question = "What went well today?",
                placeholder = "Share your wins, big or small...",
                value = uiState.wentWell,
                onValueChange = viewModel::onWentWellChange
            )

            ReflectionCard(
                icon = androidx.compose.material.icons.Icons.Outlined.TrackChanges,
                question = "What distracted you most?",
                placeholder = "No judgment — just awareness...",
                value = uiState.distracted,
                onValueChange = viewModel::onDistractedChange
            )

            ReflectionCard(
                icon = androidx.compose.material.icons.Icons.Outlined.Spa,
                question = "What could make tomorrow better?",
                placeholder = "One small improvement...",
                value = uiState.improveTomorrow,
                onValueChange = viewModel::onImproveTomorrowChange
            )

            Button(
                onClick = { viewModel.saveReflection() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SageGreen,
                    contentColor = Background
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    if (uiState.isEditingExisting) "Update Reflection" else "Save Reflection",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ReflectionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    question: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    MomentumCard {
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SageGreen,
                modifier = Modifier.size(20.dp)
            )
            Text(
                question,
                style = MaterialTheme.typography.titleSmall,
                color = OnSurface,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, style = MaterialTheme.typography.bodySmall) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 6,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SageGreen,
                unfocusedBorderColor = SurfaceContainerHighest,
                cursorColor = SageGreen,
                unfocusedContainerColor = SurfaceContainerHigh,
                focusedContainerColor = SurfaceContainerHigh
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        )
    }
}

private fun energyLabel(level: Int): String = when (level) {
    1 -> "1 — Drained"
    2 -> "2 — Tired"
    3 -> "3 — Okay"
    4 -> "4 — Energized"
    5 -> "5 — Peak Energy"
    else -> "$level"
}
