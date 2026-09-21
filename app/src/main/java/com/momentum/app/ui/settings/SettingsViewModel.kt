package com.momentum.app.ui.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momentum.app.data.repository.BackupRepository
import com.momentum.app.data.repository.SettingsRepository
import com.momentum.app.domain.model.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class SettingsUiState(
    val settings: AppSettings = AppSettings(),
    val isExporting: Boolean = false,
    val isImporting: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val backupRepository: BackupRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = settingsRepository.getSettings()
        .map { settings -> SettingsUiState(settings = settings) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _showRestoreConfirmDialog = MutableStateFlow(false)
    val showRestoreConfirmDialog: StateFlow<Boolean> = _showRestoreConfirmDialog.asStateFlow()

    private var pendingImportUri: Uri? = null

    fun getSuggestedBackupFileName(): String {
        val dateStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return "momentum_backup_$dateStr.json"
    }

    fun exportBackup(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val json = backupRepository.exportBackupJson()
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(json.toByteArray(Charsets.UTF_8))
                }
                _message.value = "Backup exported successfully"
            } catch (e: Exception) {
                _message.value = "Failed to export backup: ${e.localizedMessage ?: "Unknown error"}"
            }
        }
    }

    fun onImportUriSelected(uri: Uri) {
        pendingImportUri = uri
        _showRestoreConfirmDialog.value = true
    }

    fun dismissRestoreDialog() {
        pendingImportUri = null
        _showRestoreConfirmDialog.value = false
    }

    fun confirmRestore(context: Context) {
        val uri = pendingImportUri ?: return
        _showRestoreConfirmDialog.value = false
        pendingImportUri = null

        viewModelScope.launch {
            try {
                val json = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.bufferedReader(Charsets.UTF_8).readText()
                } ?: throw IllegalArgumentException("Could not read backup file")

                backupRepository.importBackupJson(json)
                _message.value = "Data restored successfully"
            } catch (e: Exception) {
                _message.value = "Restore failed: ${e.localizedMessage ?: "Corrupted file"}"
            }
        }
    }

    fun updateEveningReminder(context: Context, enabled: Boolean, hour: Int, minute: Int) {
        viewModelScope.launch {
            if (enabled) {
                com.momentum.app.util.ReminderScheduler.scheduleDailyReminder(context, hour, minute)
            } else {
                com.momentum.app.util.ReminderScheduler.cancelReminder(context)
            }
            settingsRepository.updateEveningReminder(enabled, hour, minute)
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
