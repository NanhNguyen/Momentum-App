package com.momentum.app.ui.balance

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momentum.app.domain.model.EntertainmentCategory
import com.momentum.app.domain.model.EntertainmentLog
import com.momentum.app.ui.components.MomentumCard
import com.momentum.app.ui.components.SectionHeader
import com.momentum.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(
    onNavigateBack: () -> Unit,
    viewModel: BalanceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeepWorkInfo by remember { mutableStateOf(false) }
    var showReferenceDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    if (showDeepWorkInfo) {
        DeepWorkInfoDialog(onDismiss = { showDeepWorkInfo = false })
    }

    if (showReferenceDialog) {
        WeeklyReferenceDialog(
            currentHours = uiState.weeklyPlayReferenceHours,
            onSave = { hours -> viewModel.updateWeeklyReferenceHours(hours) },
            onDismiss = { showReferenceDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Life Balance",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = OnSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // 1. "Today at a glance" header
            item {
                TodayAtAGlanceHeader(
                    summaryText = uiState.todayProgress.toDisplayText()
                )
            }

            // 2. Repositioned "Logged Today" section (visible without scrolling past form)
            item {
                SectionHeader(
                    title = "Logged Today",
                    subtitle = if (uiState.todayLogs.isEmpty()) "No sessions logged yet today"
                    else "${uiState.todayLogs.size} recorded sessions"
                )
            }

            if (uiState.todayLogs.isEmpty()) {
                item {
                    MomentumCard {
                        Text(
                            text = "No intentional leisure or rest recorded today yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceSubtle,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            } else {
                items(uiState.todayLogs, key = { it.id }) { log ->
                    LogItemRow(
                        log = log,
                        onDelete = { viewModel.deleteLog(log.id) }
                    )
                }
            }

            // 3. Weekly Balance Breakdown Card (3 segments, empty state teaches shape, info dialog)
            item {
                WeeklyBalanceCard(
                    deepWorkMinutes = uiState.deepWorkMinutesWeek,
                    entertainmentMinutes = uiState.entertainmentMinutesWeek,
                    restMinutes = uiState.restMinutesWeek,
                    weeklyReferenceHours = uiState.weeklyPlayReferenceHours,
                    onOpenInfo = { showDeepWorkInfo = true },
                    onOpenReferenceDialog = { showReferenceDialog = true }
                )
            }

            // 4. Quick Log Card with "Chosen on purpose?" reflection chip
            item {
                MomentumCard {
                    SectionHeader(
                        title = "Log Intentional Time",
                        subtitle = "Track what you enjoy with full presence"
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.labelMedium,
                        color = OnSurfaceSubtle
                    )
                    Spacer(Modifier.height(8.dp))

                    // Category Grid / Row
                    CategorySelector(
                        selectedCategory = uiState.selectedCategory,
                        onSelectCategory = viewModel::onCategorySelect
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Duration",
                        style = MaterialTheme.typography.labelMedium,
                        color = OnSurfaceSubtle
                    )
                    Spacer(Modifier.height(8.dp))

                    // Quick duration buttons
                    DurationSelector(
                        selectedMinutes = uiState.selectedDurationMinutes,
                        isCustom = uiState.isCustomDuration,
                        customText = uiState.customMinutesText,
                        onQuickSelect = viewModel::onQuickDurationSelect,
                        onCustomChange = viewModel::onCustomDurationChange
                    )

                    Spacer(Modifier.height(16.dp))

                    // Optional note
                    OutlinedTextField(
                        value = uiState.note,
                        onValueChange = viewModel::onNoteChange,
                        label = { Text("Note (optional)") },
                        placeholder = { Text("e.g., Chill match with friends, documentary...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SageGreen,
                            unfocusedBorderColor = SurfaceContainerHighest,
                            focusedLabelColor = SageGreen,
                            unfocusedLabelColor = OnSurfaceSubtle,
                            cursorColor = SageGreen
                        ),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    // 5. "Chosen on purpose?" reflection chips (optional)
                    Text(
                        text = "Chosen on purpose? (optional)",
                        style = MaterialTheme.typography.labelMedium,
                        color = OnSurfaceSubtle
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilterChip(
                            selected = uiState.isIntentional == true,
                            onClick = { viewModel.onIntentionalSelect(true) },
                            label = { Text("Yes") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SageGreenContainer,
                                selectedLabelColor = SageGreen,
                                containerColor = SurfaceContainerHigh,
                                labelColor = OnSurface
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = uiState.isIntentional == true,
                                borderColor = SurfaceContainerHighest,
                                selectedBorderColor = SageGreen
                            )
                        )
                        FilterChip(
                            selected = uiState.isIntentional == false,
                            onClick = { viewModel.onIntentionalSelect(false) },
                            label = { Text("Not sure") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SteelBlue.copy(alpha = 0.2f),
                                selectedLabelColor = SteelBlue,
                                containerColor = SurfaceContainerHigh,
                                labelColor = OnSurface
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = uiState.isIntentional == false,
                                borderColor = SurfaceContainerHighest,
                                selectedBorderColor = SteelBlue
                            )
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = viewModel::logActivity,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SageGreen,
                            contentColor = Background
                        )
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Save Intentional Time",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TodayAtAGlanceHeader(summaryText: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfaceContainerHigh,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Spa,
                contentDescription = null,
                tint = SageGreen,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = summaryText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = OnSurface
            )
        }
    }
}

@Composable
fun WeeklyBalanceCard(
    deepWorkMinutes: Int,
    entertainmentMinutes: Int,
    restMinutes: Int,
    weeklyReferenceHours: Int?,
    onOpenInfo: () -> Unit,
    onOpenReferenceDialog: () -> Unit
) {
    val totalMinutes = deepWorkMinutes + entertainmentMinutes + restMinutes

    val targetDeepWork = if (totalMinutes > 0) deepWorkMinutes.toFloat() / totalMinutes else 0f
    val targetEnt = if (totalMinutes > 0) entertainmentMinutes.toFloat() / totalMinutes else 0f
    val targetRest = if (totalMinutes > 0) restMinutes.toFloat() / totalMinutes else 0f

    val animDeepWork by animateFloatAsState(
        targetValue = targetDeepWork,
        animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing),
        label = "animDeepWork"
    )
    val animEnt by animateFloatAsState(
        targetValue = targetEnt,
        animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing),
        label = "animEnt"
    )
    val animRest by animateFloatAsState(
        targetValue = targetRest,
        animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing),
        label = "animRest"
    )

    val deepHours = deepWorkMinutes / 60
    val deepMins = deepWorkMinutes % 60
    val deepText = if (deepHours > 0) "${deepHours}h ${deepMins}m" else "${deepMins}m"

    val entHours = entertainmentMinutes / 60
    val entMins = entertainmentMinutes % 60
    val entText = if (entHours > 0) "${entHours}h ${entMins}m" else "${entMins}m"

    val restHours = restMinutes / 60
    val restMins = restMinutes % 60
    val restText = if (restHours > 0) "${restHours}h ${restMins}m" else "${restMins}m"

    val insightQuote = when {
        entertainmentMinutes > 0 || restMinutes > 0 ->
            "$entText intentional entertainment this week. Rest is part of the process."
        totalMinutes == 0 ->
            "Start logging your week to visualize your balance between focus and rest."
        else ->
            "Good focus this week! Remember to take intentional time to recharge."
    }

    MomentumCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SectionHeader(
                    title = "Weekly Balance Breakdown",
                    subtitle = "Focus, intentional play & recovery this week"
                )
            }
            IconButton(
                onClick = onOpenInfo,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Deep work formula info",
                    tint = OnSurfaceSubtle,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Progress bar: empty state teaches the shape vs data-filled state
        if (totalMinutes == 0) {
            // Empty state: 3 equal preview segments with subtle tints & dividers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(SurfaceContainerHighest),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(SageGreen.copy(alpha = 0.25f))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(SteelBlue.copy(alpha = 0.25f))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(WarmSand.copy(alpha = 0.25f))
                )
            }
        } else {
            // Filled state: animated 3 segments
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(SurfaceContainerHighest)
            ) {
                if (animDeepWork > 0f) {
                    Box(
                        modifier = Modifier
                            .weight(animDeepWork.coerceAtLeast(0.001f))
                            .fillMaxHeight()
                            .background(SageGreen)
                    )
                }
                if (animEnt > 0f) {
                    Box(
                        modifier = Modifier
                            .weight(animEnt.coerceAtLeast(0.001f))
                            .fillMaxHeight()
                            .background(SteelBlue)
                    )
                }
                if (animRest > 0f) {
                    Box(
                        modifier = Modifier
                            .weight(animRest.coerceAtLeast(0.001f))
                            .fillMaxHeight()
                            .background(WarmSand)
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Labels / Legend: always shows all 3 categories with hours/mins even when zero
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LegendItem(
                label = "Deep Work",
                durationText = if (totalMinutes == 0) "0h" else deepText,
                percent = if (totalMinutes == 0) 0 else (targetDeepWork * 100).toInt(),
                color = SageGreen
            )
            LegendItem(
                label = "Play",
                durationText = if (totalMinutes == 0) "0h" else entText,
                percent = if (totalMinutes == 0) 0 else (targetEnt * 100).toInt(),
                color = SteelBlue
            )
            LegendItem(
                label = "Rest",
                durationText = if (totalMinutes == 0) "0h" else restText,
                percent = if (totalMinutes == 0) 0 else (targetRest * 100).toInt(),
                color = WarmSand
            )
        }

        // Optional personal balance reference pace marker
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { onOpenReferenceDialog() }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Tune,
                    contentDescription = null,
                    tint = SteelBlue,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = if (weeklyReferenceHours != null) {
                        "Your usual pace: ${weeklyReferenceHours}h/wk play"
                    } else {
                        "Set weekly play reference (optional)"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceSubtle
                )
            }
            if (weeklyReferenceHours != null) {
                val entHoursCurrent = entertainmentMinutes / 60
                val entMinsCurrent = entertainmentMinutes % 60
                val curPace = if (entHoursCurrent > 0) "${entHoursCurrent}h ${entMinsCurrent}m" else "${entMinsCurrent}m"
                Text(
                    text = "$curPace logged",
                    style = MaterialTheme.typography.labelSmall,
                    color = SteelBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Non-judgmental message banner
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = SurfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Spa,
                    contentDescription = null,
                    tint = SageGreen,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = insightQuote,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurface
                )
            }
        }
    }
}

@Composable
fun DeepWorkInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "How Deep Work is calculated",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Deep Work = completed Big 3 tasks + habit check-ins",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = SageGreen
                )
                Text(
                    text = "Each completed Big 3 task counts as ~90 minutes of focused effort, other tasks count as ~45 minutes, and completed habits count as ~20 minutes.\n\nThis provides an encouraging, realistic estimate of your weekly focus alongside your rest — without needing a stopwatch.",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceMuted
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got it", color = SageGreen)
            }
        },
        containerColor = SurfaceContainerHigh
    )
}

@Composable
fun WeeklyReferenceDialog(
    currentHours: Int?,
    onSave: (Int?) -> Unit,
    onDismiss: () -> Unit
) {
    var textValue by remember { mutableStateOf(currentHours?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Personal Weekly Reference",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Set a gentle weekly target for entertainment & play time. This is purely a personal reference point — never a limit or budget.",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceMuted
                )
                OutlinedTextField(
                    value = textValue,
                    onValueChange = { input -> textValue = input.filter { it.isDigit() }.take(3) },
                    label = { Text("Hours per week") },
                    placeholder = { Text("e.g. 8") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SageGreen,
                        unfocusedBorderColor = SurfaceContainerHighest,
                        focusedLabelColor = SageGreen,
                        cursorColor = SageGreen
                    ),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsed = textValue.toIntOrNull()
                    onSave(if (parsed != null && parsed > 0) parsed else null)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SageGreen, contentColor = Background)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onSave(null)
                    onDismiss()
                }
            ) {
                Text("Clear", color = OnSurfaceSubtle)
            }
        },
        containerColor = SurfaceContainerHigh
    )
}

@Composable
fun LegendItem(
    label: String,
    durationText: String,
    percent: Int,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Column {
            Text(
                text = "$label ($percent%)",
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceSubtle
            )
            Text(
                text = durationText,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = OnSurface
            )
        }
    }
}

@Composable
fun CategorySelector(
    selectedCategory: EntertainmentCategory,
    onSelectCategory: (EntertainmentCategory) -> Unit
) {
    val categories = EntertainmentCategory.entries

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.chunked(3).forEach { rowCategories ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowCategories.forEach { category ->
                    val isSelected = category == selectedCategory
                    val icon = getCategoryIcon(category)

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectCategory(category) }
                            .border(
                                width = 1.dp,
                                color = if (isSelected) SageGreen else SurfaceContainerHighest,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        color = if (isSelected) SageGreenContainer else SurfaceContainerHigh
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isSelected) SageGreen else OnSurfaceSubtle
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = category.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) SageGreen else OnSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DurationSelector(
    selectedMinutes: Int,
    isCustom: Boolean,
    customText: String,
    onQuickSelect: (Int) -> Unit,
    onCustomChange: (String) -> Unit
) {
    val options = listOf(
        15 to "15m",
        30 to "30m",
        45 to "45m",
        60 to "1h",
        120 to "2h"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(options) { (minutes, label) ->
                val isSelected = !isCustom && selectedMinutes == minutes
                FilterChip(
                    selected = isSelected,
                    onClick = { onQuickSelect(minutes) },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SageGreenContainer,
                        selectedLabelColor = SageGreen,
                        containerColor = SurfaceContainerHigh,
                        labelColor = OnSurface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = SurfaceContainerHighest,
                        selectedBorderColor = SageGreen
                    )
                )
            }
        }

        // Custom duration input
        OutlinedTextField(
            value = customText,
            onValueChange = onCustomChange,
            label = { Text("Or custom minutes") },
            placeholder = { Text("e.g. 90") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SageGreen,
                unfocusedBorderColor = if (isCustom && customText.isNotBlank()) SageGreen else SurfaceContainerHighest,
                focusedLabelColor = SageGreen,
                unfocusedLabelColor = OnSurfaceSubtle,
                cursorColor = SageGreen
            ),
            singleLine = true
        )
    }
}

@Composable
fun LogItemRow(
    log: EntertainmentLog,
    onDelete: () -> Unit
) {
    val hours = log.durationMinutes / 60
    val mins = log.durationMinutes % 60
    val durStr = if (hours > 0) "${hours}h ${mins}m" else "${mins}m"

    MomentumCard {
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
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (log.category == EntertainmentCategory.REST) WarmSand.copy(alpha = 0.2f)
                            else SteelBlue.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIcon(log.category),
                        contentDescription = null,
                        tint = if (log.category == EntertainmentCategory.REST) WarmSand else SteelBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = log.category.label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface
                        )
                        if (log.isIntentional != null) {
                            val tagLabel = if (log.isIntentional) "Intentional" else "Not sure"
                            val tagColor = if (log.isIntentional) SageGreen else SteelBlue
                            val tagBg = if (log.isIntentional) SageGreenContainer else SteelBlue.copy(alpha = 0.2f)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(tagBg)
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = tagLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = tagColor,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                    if (!log.note.isNullOrBlank()) {
                        Text(
                            text = log.note,
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceSubtle
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = durStr,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = SageGreen
                )
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Remove entry",
                        tint = OnSurfaceSubtle,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

fun getCategoryIcon(category: EntertainmentCategory): ImageVector {
    return when (category) {
        EntertainmentCategory.GAMING -> Icons.Filled.SportsEsports
        EntertainmentCategory.YOUTUBE -> Icons.Filled.PlayCircle
        EntertainmentCategory.NETFLIX -> Icons.Filled.Tv
        EntertainmentCategory.SOCIAL_MEDIA -> Icons.Filled.Forum
        EntertainmentCategory.REST -> Icons.Filled.SelfImprovement
        EntertainmentCategory.OTHER -> Icons.Filled.MoreHoriz
    }
}
