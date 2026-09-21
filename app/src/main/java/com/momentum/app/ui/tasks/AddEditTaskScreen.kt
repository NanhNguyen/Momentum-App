package com.momentum.app.ui.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momentum.app.domain.model.RecurrenceRule
import com.momentum.app.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: Long? = null,
    onNavigateBack: () -> Unit,
    viewModel: AddEditTaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDatePicker by remember { mutableStateOf(false) }

    // Load existing task if editing
    LaunchedEffect(taskId) {
        if (taskId != null && taskId != -1L) {
            viewModel.loadTask(taskId)
        }
    }

    // Navigate back when saved
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onNavigateBack()
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.isEditMode) "Edit Task" else "New Task",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onSaveClick() }) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Save task",
                            tint = SageGreen
                        )
                    }
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title input
            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Task title") },
                placeholder = { Text("What do you want to get done?") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.errorMessage != null,
                supportingText = if (uiState.errorMessage != null) {
                    { Text(uiState.errorMessage!!, color = MaterialTheme.colorScheme.error) }
                } else null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SageGreen,
                    focusedLabelColor = SageGreen,
                    cursorColor = SageGreen
                )
            )

            // Description
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Notes (optional)") },
                placeholder = { Text("Any details or context...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SageGreen,
                    focusedLabelColor = SageGreen,
                    cursorColor = SageGreen
                )
            )

            // Date selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Scheduled Date",
                    style = MaterialTheme.typography.labelLarge,
                    color = OnSurfaceMuted
                )
                val today = LocalDate.now()
                val tomorrow = today.plusDays(1)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = uiState.date == today,
                        onClick = { viewModel.onDateChange(today) },
                        label = { Text("Today") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SageGreenContainer,
                            selectedLabelColor = SageGreen
                        )
                    )
                    FilterChip(
                        selected = uiState.date == tomorrow,
                        onClick = { viewModel.onDateChange(tomorrow) },
                        label = { Text("Tomorrow") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SageGreenContainer,
                            selectedLabelColor = SageGreen
                        )
                    )
                    FilterChip(
                        selected = uiState.date != today && uiState.date != tomorrow,
                        onClick = { showDatePicker = true },
                        label = {
                            Text(
                                if (uiState.date != today && uiState.date != tomorrow)
                                    uiState.date.format(DateTimeFormatter.ofPattern("MMM d"))
                                else "Pick date"
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SageGreenContainer,
                            selectedLabelColor = SageGreen
                        )
                    )
                }
            }

            // Priority selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Priority",
                    style = MaterialTheme.typography.labelLarge,
                    color = OnSurfaceMuted
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0 to "Low", 1 to "Medium", 2 to "High").forEach { (value, label) ->
                        FilterChip(
                            selected = uiState.priority == value,
                            onClick = { viewModel.onPriorityChange(value) },
                            label = { Text(label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = when (value) {
                                    2 -> PriorityHigh.copy(alpha = 0.2f)
                                    1 -> PriorityMedium.copy(alpha = 0.2f)
                                    else -> SurfaceContainerHighest
                                },
                                selectedLabelColor = when (value) {
                                    2 -> PriorityHigh
                                    1 -> PriorityMedium
                                    else -> OnSurfaceMuted
                                }
                            )
                        )
                    }
                }
            }

            // Repeat selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Repeat",
                    style = MaterialTheme.typography.labelLarge,
                    color = OnSurfaceMuted
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = !uiState.isRecurring,
                        onClick = { viewModel.onRecurrenceRuleChange(null) },
                        label = { Text("Off") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SageGreenContainer,
                            selectedLabelColor = SageGreen
                        )
                    )
                    FilterChip(
                        selected = uiState.isRecurring && uiState.recurrenceRule == RecurrenceRule.DAILY,
                        onClick = { viewModel.onRecurrenceRuleChange(RecurrenceRule.DAILY) },
                        label = { Text("Daily") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SageGreenContainer,
                            selectedLabelColor = SageGreen
                        )
                    )
                    FilterChip(
                        selected = uiState.isRecurring && uiState.recurrenceRule == RecurrenceRule.WEEKDAYS,
                        onClick = { viewModel.onRecurrenceRuleChange(RecurrenceRule.WEEKDAYS) },
                        label = { Text("Weekdays") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SageGreenContainer,
                            selectedLabelColor = SageGreen
                        )
                    )
                }
            }

            // Big 3 toggle
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.isBigThree) SageGreenContainer else SurfaceContainer
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (uiState.isBigThree) SageGreen else SurfaceContainerHighest
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Mark as Big 3",
                            style = MaterialTheme.typography.titleSmall,
                            color = if (uiState.isBigThree) SageGreen else OnSurface,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "One of your top 3 focus tasks for today",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceSubtle
                        )
                    }
                    Switch(
                        checked = uiState.isBigThree,
                        onCheckedChange = { viewModel.onBigThreeToggle(it) },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = SageGreen,
                            checkedThumbColor = Background
                        )
                    )
                }
            }

            // Big 3 limit message (gentle, not alarming)
            if (uiState.bigThreeLimitReached) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = WarmSand.copy(alpha = 0.1f)),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmSand.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Outlined.TrackChanges,
                                contentDescription = null,
                                tint = WarmSand,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                "Your Big 3 is full",
                                style = MaterialTheme.typography.titleSmall,
                                color = WarmSand,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "You've already chosen 3 focus tasks for today. " +
                                    "Keeping focus on fewer things helps you do them better. " +
                                    "This task will still be saved — just not as a Big 3.",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceSubtle
                        )
                    }
                }
            }

            // Save button
            Button(
                onClick = { viewModel.onSaveClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SageGreen,
                    contentColor = Background
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Text(
                    if (uiState.isEditMode) "Update Task" else "Save Task",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }

    // Recurring task edit confirmation dialog
    if (uiState.showRecurringEditDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissRecurringEditDialog() },
            title = {
                Text(
                    "Edit recurring task",
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface
                )
            },
            text = {
                Text(
                    "Do you want to apply these changes only to today, or to this and all future occurrences?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceSubtle
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.executeSave(updateFuture = true) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SageGreen,
                        contentColor = Background
                    )
                ) {
                    Text("This & future", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.executeSave(updateFuture = false) }) {
                    Text("Just today", color = OnSurfaceSubtle)
                }
            },
            containerColor = SurfaceContainerHigh
        )
    }

    if (showDatePicker) {
        val initialMillis = uiState.date.atStartOfDay(java.time.ZoneId.of("UTC")).toInstant().toEpochMilli()
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneId.of("UTC"))
                                .toLocalDate()
                            viewModel.onDateChange(selectedDate)
                        }
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = SageGreen)
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            },
            colors = DatePickerDefaults.colors(containerColor = SurfaceContainer)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = SageGreen,
                    todayDateBorderColor = SageGreen
                )
            )
        }
    }
}
