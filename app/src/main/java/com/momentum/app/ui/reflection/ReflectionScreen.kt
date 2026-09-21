package com.momentum.app.ui.reflection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momentum.app.domain.model.ReflectionEntry
import com.momentum.app.ui.components.*
import com.momentum.app.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Unified Daily Reflection & Journal Feed.
 * Merges the Today prompt/form/summary and the timeline of past reflections into one continuous screen.
 * Today's entry appears at the top as the hero card once saved, followed by past days below.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReflectionScreen(
    viewModel: ReflectionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedPastEntry by remember { mutableStateOf<ReflectionEntry?>(null) }

    // Intercept system back press when in editing mode to safely discard uncommitted edits
    val isEditing = uiState.todayState is TodayReflectionState.Editing
    BackHandler(enabled = isEditing) {
        viewModel.discardChanges()
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
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
        // Single continuous scrollable feed
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
        ) {
            // ─── 1. Today Section (Prompt, Form, or Summary Card) ───
            item(key = "today_section") {
                when (val state = uiState.todayState) {
                    is TodayReflectionState.Prompt -> {
                        TodayPromptCard(onStartWriting = viewModel::startWritingToday)
                    }

                    is TodayReflectionState.Editing -> {
                        TodayEditingForm(
                            state = state,
                            viewModel = viewModel
                        )
                    }

                    is TodayReflectionState.Saved -> {
                        ReflectionSummaryCard(
                            entry = state.entry,
                            isToday = true,
                            onEditClick = viewModel::startEditingExisting
                        )
                    }
                }
            }

            // ─── 2. Timeline Section (Past Entries) ───
            if (uiState.pastReflections.isNotEmpty()) {
                item(key = "timeline_header") {
                    Text(
                        text = "Past Reflections",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }

                items(uiState.pastReflections, key = { it.id }) { entry ->
                    ReflectionHistoryItem(
                        entry = entry,
                        onClick = { selectedPastEntry = entry }
                    )
                }
            } else if (uiState.todayState !is TodayReflectionState.Saved) {
                // If there are truly no entries anywhere yet in the journal
                item(key = "empty_timeline") {
                    TimelineEmptyState()
                }
            }
        }

        // ModalBottomSheet for past reflections read-only detail view
        if (selectedPastEntry != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedPastEntry = null },
                containerColor = SurfaceContainerHigh
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 32.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    ReflectionSummaryCard(
                        entry = selectedPastEntry!!,
                        isToday = false,
                        onEditClick = null
                    )
                }
            }
        }
    }
}

/** Compact prompt card shown when no entry has been created yet today */
@Composable
private fun TodayPromptCard(
    onStartWriting: () -> Unit
) {
    MomentumCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onStartWriting)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SageGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.EditNote,
                        contentDescription = null,
                        tint = SageGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "How was your day?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface
                    )
                    Text(
                        text = "Tap to write today's reflection",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceSubtle
                    )
                }
            }

            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Start writing",
                tint = SageGreen,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/** Reflection form card for drafting or editing today's entry */
@Composable
private fun TodayEditingForm(
    state: TodayReflectionState.Editing,
    viewModel: ReflectionViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.EditNote,
                contentDescription = null,
                tint = SageGreen,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = if (state.isEditingExisting) "Edit Today's Reflection" else "Today's Reflection",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = OnSurface
            )
        }

        Text(
            text = "Take a moment to look back. No judgment, just awareness.",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceSubtle
        )

        // Mood Picker Card with subtle error highlight if attempted to save without selecting
        MomentumCard(
            modifier = if (state.moodError) {
                Modifier.border(1.dp, SoftError, RoundedCornerShape(16.dp))
            } else Modifier
        ) {
            Text(
                "How was your day?",
                style = MaterialTheme.typography.titleSmall,
                color = OnSurface,
                fontWeight = FontWeight.Medium
            )

            if (state.moodError) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "Please select how your day was",
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftError
                )
            }

            Spacer(Modifier.height(12.dp))
            MoodPicker(
                selectedMood = state.draft.mood,
                onMoodSelect = viewModel::onMoodSelect
            )
        }

        // Energy Level Slider
        MomentumCard {
            Text(
                "Energy Level",
                style = MaterialTheme.typography.titleSmall,
                color = OnSurface,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            Slider(
                value = state.draft.energyLevel.toFloat(),
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
                    energyLabel(state.draft.energyLevel),
                    style = MaterialTheme.typography.labelSmall,
                    color = SageGreen
                )
                Text("High", style = MaterialTheme.typography.labelSmall, color = OnSurfaceSubtle)
            }
        }

        // 3 Reflection Questions
        ReflectionQuestionCard(
            icon = Icons.Outlined.AutoAwesome,
            question = "What went well today?",
            placeholder = "Share your wins, big or small...",
            value = state.draft.wentWell,
            onValueChange = viewModel::onWentWellChange
        )

        ReflectionQuestionCard(
            icon = Icons.Outlined.TrackChanges,
            question = "What distracted you most?",
            placeholder = "No judgment — just awareness...",
            value = state.draft.distracted,
            onValueChange = viewModel::onDistractedChange
        )

        ReflectionQuestionCard(
            icon = Icons.Outlined.Spa,
            question = "What could make tomorrow better?",
            placeholder = "One small improvement...",
            value = state.draft.improveTomorrow,
            onValueChange = viewModel::onImproveTomorrowChange
        )

        // Save Button (always shown)
        Button(
            onClick = { viewModel.saveReflection() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = SageGreen,
                contentColor = Background
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                "Save Reflection",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Discard Changes Button (ONLY shown when editing an existing entry)
        if (state.isEditingExisting) {
            OutlinedButton(
                onClick = { viewModel.discardChanges() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = OnSurfaceSubtle
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Discard changes",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun ReflectionQuestionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    question: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    MomentumCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
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
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
private fun ReflectionHistoryItem(
    entry: ReflectionEntry,
    onClick: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy")
    val dateText = entry.date.format(dateFormatter)

    val snippet = entry.wentWell?.takeIf { it.isNotBlank() }
        ?: entry.improveTomorrow?.takeIf { it.isNotBlank() }
        ?: entry.distracted?.takeIf { it.isNotBlank() }
        ?: "Reflection completed"

    MomentumCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface
                )

                // Mood and Energy row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = entry.mood.icon,
                        contentDescription = entry.mood.label,
                        tint = SageGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = entry.mood.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = SageGreen,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelMedium,
                        color = OnSurfaceSubtle
                    )
                    Text(
                        text = "Energy ${entry.energyLevel}/5",
                        style = MaterialTheme.typography.labelMedium,
                        color = OnSurfaceSubtle
                    )
                }

                // Snippet of "What went well today?"
                Text(
                    text = snippet,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceSubtle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = OnSurfaceSubtle,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun TimelineEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(SurfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = null,
                tint = SageGreen,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Your reflections will show up here once you start writing.",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = OnSurface,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Take a moment each evening to pause and reflect on your day.",
            style = MaterialTheme.typography.bodySmall,
            color = OnSurfaceSubtle,
            textAlign = TextAlign.Center
        )
    }
}
