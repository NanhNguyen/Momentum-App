package com.momentum.app.ui.settings

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.momentum.app.ui.components.MomentumCard
import com.momentum.app.ui.components.SectionHeader
import com.momentum.app.ui.theme.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val showRestoreDialog by viewModel.showRestoreConfirmDialog.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    // SAF Launchers
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            viewModel.exportBackup(context, uri)
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            viewModel.onImportUriSelected(uri)
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updateEveningReminder(
            context = context,
            enabled = isGranted,
            hour = uiState.settings.eveningReminderHour,
            minute = uiState.settings.eveningReminderMinute
        )
    }

    // Confirmation Dialog before Restoring
    if (showRestoreDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissRestoreDialog() },
            title = {
                Text(
                    text = "Restore Data?",
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurface
                )
            },
            text = {
                Text(
                    text = "This will replace all current tasks, habits, reflections, and leisure logs with the backup file data. This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceSubtle
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmRestore(context) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SageGreen,
                        contentColor = Background
                    )
                ) {
                    Text("Restore", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissRestoreDialog() }) {
                    Text("Cancel", color = OnSurfaceSubtle)
                }
            },
            containerColor = SurfaceContainerHigh
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
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
            // Section: Data & Backup
            item {
                SectionHeader(
                    title = "Data & Backup",
                    subtitle = "100% offline & local. Export or restore your data anytime."
                )
            }

            item {
                MomentumCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FileDownload,
                            contentDescription = null,
                            tint = SageGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Export Backup",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface
                            )
                            Text(
                                text = "Save a complete JSON copy of your habits, tasks, and reflections",
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceSubtle
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Button(
                        onClick = {
                            exportLauncher.launch(viewModel.getSuggestedBackupFileName())
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SageGreen,
                            contentColor = Background
                        )
                    ) {
                        Icon(Icons.Outlined.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Export JSON Backup", fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = SurfaceContainerHighest, thickness = 0.5.dp)
                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FileUpload,
                            contentDescription = null,
                            tint = SteelBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Restore Backup",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface
                            )
                            Text(
                                text = "Import data from a previously exported Momentum JSON file",
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceSubtle
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = {
                            importLauncher.launch(arrayOf("application/json"))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SteelBlue),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SteelBlue)
                    ) {
                        Icon(Icons.Outlined.FileUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Select Backup File to Restore", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Section: Gentle Evening Reminder
            item {
                SectionHeader(
                    title = "Notifications & Reminders",
                    subtitle = "Gentle daily reminder for self-reflection"
                )
            }

            item {
                val reminderTime = LocalTime.of(
                    uiState.settings.eveningReminderHour,
                    uiState.settings.eveningReminderMinute
                )
                val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
                val formattedTime = reminderTime.format(timeFormatter)

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
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = null,
                                tint = SageGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = "Evening Reflection Reminder",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurface
                                )
                                Text(
                                    text = "A calm reminder to reflect on your day",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceSubtle
                                )
                            }
                        }

                        Switch(
                            checked = uiState.settings.isEveningReminderEnabled,
                            onCheckedChange = { isEnabled ->
                                if (isEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val hasPermission = ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) == PackageManager.PERMISSION_GRANTED
                                    if (!hasPermission) {
                                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        return@Switch
                                    }
                                }
                                viewModel.updateEveningReminder(
                                    context = context,
                                    enabled = isEnabled,
                                    hour = uiState.settings.eveningReminderHour,
                                    minute = uiState.settings.eveningReminderMinute
                                )
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Background,
                                checkedTrackColor = SageGreen,
                                uncheckedThumbColor = OnSurfaceSubtle,
                                uncheckedTrackColor = SurfaceContainerHighest
                            )
                        )
                    }

                    if (uiState.settings.isEveningReminderEnabled) {
                        Spacer(Modifier.height(14.dp))
                        HorizontalDivider(color = SurfaceContainerHighest, thickness = 0.5.dp)
                        Spacer(Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    TimePickerDialog(
                                        context,
                                        { _, hourOfDay, minute ->
                                            viewModel.updateEveningReminder(
                                                context = context,
                                                enabled = true,
                                                hour = hourOfDay,
                                                minute = minute
                                            )
                                        },
                                        uiState.settings.eveningReminderHour,
                                        uiState.settings.eveningReminderMinute,
                                        false
                                    ).show()
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Schedule,
                                    contentDescription = null,
                                    tint = OnSurfaceSubtle,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Reminder Time",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurface
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SurfaceContainerHigh
                            ) {
                                Text(
                                    text = formattedTime,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SageGreen,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Section: About
            item {
                SectionHeader(title = "About Momentum")
            }

            item {
                MomentumCard {
                    Text(
                        text = "Momentum",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Version 1.0.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = SageGreen
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Progress over perfection. Momentum is designed to help you build habits, focus on what matters most, and live with intentional balance.\n\nAll data is stored purely locally on your device with no remote analytics or tracking.",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceSubtle,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
