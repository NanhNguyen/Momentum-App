package com.momentum.app.ui.balance

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
import androidx.compose.material.icons.outlined.Spa
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

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
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
            // Philosophy quote
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = SageGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Don't eliminate entertainment — make it intentional.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceMuted
                    )
                }
            }

            // Weekly Balance Breakdown Card
            item {
                WeeklyBalanceCard(
                    deepWorkMinutes = uiState.deepWorkMinutesWeek,
                    entertainmentMinutes = uiState.entertainmentMinutesWeek,
                    restMinutes = uiState.restMinutesWeek
                )
            }

            // Quick Log Card
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

            // Logged Today Section
            item {
                SectionHeader(
                    title = "Logged Today",
                    subtitle = "${uiState.todayLogs.size} recorded sessions"
                )
            }

            if (uiState.todayLogs.isEmpty()) {
                item {
                    MomentumCard {
                        Text(
                            text = "No intentional leisure or rest recorded today yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceSubtle,
                            modifier = Modifier.padding(vertical = 8.dp)
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
        }
    }
}

@Composable
fun WeeklyBalanceCard(
    deepWorkMinutes: Int,
    entertainmentMinutes: Int,
    restMinutes: Int
) {
    val totalMinutes = deepWorkMinutes + entertainmentMinutes + restMinutes

    val deepWorkPct = if (totalMinutes > 0) deepWorkMinutes.toFloat() / totalMinutes else 0f
    val entPct = if (totalMinutes > 0) entertainmentMinutes.toFloat() / totalMinutes else 0f
    val restPct = if (totalMinutes > 0) restMinutes.toFloat() / totalMinutes else 0f

    val entHours = entertainmentMinutes / 60
    val entMins = entertainmentMinutes % 60
    val entText = if (entHours > 0) "${entHours}h ${entMins}m" else "${entMins}m"

    val deepHours = deepWorkMinutes / 60
    val deepMins = deepWorkMinutes % 60
    val deepText = if (deepHours > 0) "${deepHours}h ${deepMins}m" else "${deepMins}m"

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
        SectionHeader(
            title = "Weekly Balance Breakdown",
            subtitle = "Focus, intentional play & recovery this week"
        )

        Spacer(Modifier.height(16.dp))

        // Stacked horizontal bar
        if (totalMinutes > 0) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceContainerHighest)
            ) {
                if (deepWorkPct > 0) {
                    Box(
                        modifier = Modifier
                            .weight(deepWorkPct)
                            .fillMaxHeight()
                            .background(SageGreen)
                    )
                }
                if (entPct > 0) {
                    Box(
                        modifier = Modifier
                            .weight(entPct)
                            .fillMaxHeight()
                            .background(SteelBlue)
                    )
                }
                if (restPct > 0) {
                    Box(
                        modifier = Modifier
                            .weight(restPct)
                            .fillMaxHeight()
                            .background(WarmSand)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LegendItem(
                    label = "Deep Work",
                    durationText = deepText,
                    percent = (deepWorkPct * 100).toInt(),
                    color = SageGreen
                )
                LegendItem(
                    label = "Entertainment",
                    durationText = entText,
                    percent = (entPct * 100).toInt(),
                    color = SteelBlue
                )
                LegendItem(
                    label = "Rest",
                    durationText = restText,
                    percent = (restPct * 100).toInt(),
                    color = WarmSand
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(SurfaceContainerHighest)
            )
        }

        Spacer(Modifier.height(14.dp))

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
                    imageVector = Icons.Default.Info,
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

                Column {
                    Text(
                        text = log.category.label,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface
                    )
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
