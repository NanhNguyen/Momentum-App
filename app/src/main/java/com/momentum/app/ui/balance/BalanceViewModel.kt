package com.momentum.app.ui.balance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momentum.app.data.repository.EntertainmentRepository
import com.momentum.app.data.repository.HabitRepository
import com.momentum.app.data.repository.SettingsRepository
import com.momentum.app.data.repository.TaskRepository
import com.momentum.app.domain.model.EntertainmentCategory
import com.momentum.app.domain.model.EntertainmentLog
import com.momentum.app.domain.model.HabitStatus
import com.momentum.app.domain.usecase.GetTodayProgressSummaryUseCase
import com.momentum.app.domain.usecase.TodayProgressSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

data class BalanceUiState(
    val selectedCategory: EntertainmentCategory = EntertainmentCategory.GAMING,
    val selectedDurationMinutes: Int = 30,
    val isCustomDuration: Boolean = false,
    val customMinutesText: String = "",
    val note: String = "",
    val isIntentional: Boolean? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val todayLogs: List<EntertainmentLog> = emptyList(),
    val weeklyLogs: List<EntertainmentLog> = emptyList(),
    val deepWorkMinutesWeek: Int = 0,
    val entertainmentMinutesWeek: Int = 0,
    val restMinutesWeek: Int = 0,
    val todayProgress: TodayProgressSummary = TodayProgressSummary(),
    val weeklyPlayReferenceHours: Int? = null,
    val message: String? = null
)

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val entertainmentRepository: EntertainmentRepository,
    private val taskRepository: TaskRepository,
    private val habitRepository: HabitRepository,
    private val settingsRepository: SettingsRepository,
    private val getTodayProgressSummaryUseCase: GetTodayProgressSummaryUseCase
) : ViewModel() {

    private val today: LocalDate get() = LocalDate.now()
    private val monday: LocalDate get() = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    private val sunday: LocalDate get() = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    private val _uiState = MutableStateFlow(BalanceUiState())
    val uiState: StateFlow<BalanceUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val weekFlow = combine(
                entertainmentRepository.getLogsForWeek(monday, sunday),
                taskRepository.getTasksForWeek(monday, sunday),
                habitRepository.getAllLogsForWeek(monday, sunday)
            ) { weeklyLogs, tasksWeek, habitLogsWeek ->
                // Calculate Deep Work minutes:
                // Completed Big 3 tasks ~ 90 mins, other completed tasks ~ 45 mins, completed habits ~ 20 mins
                val bigThreeDone = tasksWeek.count { it.isCompleted && it.isBigThree }
                val otherTasksDone = tasksWeek.count { it.isCompleted && !it.isBigThree }
                val habitsDone = habitLogsWeek.count { it.status == HabitStatus.DONE }
                val deepWorkMinutes = (bigThreeDone * 90) + (otherTasksDone * 45) + (habitsDone * 20)

                // Intentional Play vs Rest
                val restMinutes = weeklyLogs
                    .filter { it.category == EntertainmentCategory.REST }
                    .sumOf { it.durationMinutes }
                val entMinutes = weeklyLogs
                    .filter { it.category != EntertainmentCategory.REST }
                    .sumOf { it.durationMinutes }

                Triple(weeklyLogs, deepWorkMinutes, entMinutes to restMinutes)
            }

            combine(
                entertainmentRepository.getLogsForDate(today),
                weekFlow,
                getTodayProgressSummaryUseCase(today),
                settingsRepository.getSettings()
            ) { todayLogs, (weeklyLogs, deepWorkMinutes, entAndRest), todayProgress, settings ->
                val (entMinutes, restMinutes) = entAndRest
                _uiState.value.copy(
                    todayLogs = todayLogs,
                    weeklyLogs = weeklyLogs,
                    deepWorkMinutesWeek = deepWorkMinutes,
                    entertainmentMinutesWeek = entMinutes,
                    restMinutesWeek = restMinutes,
                    todayProgress = todayProgress,
                    weeklyPlayReferenceHours = settings.weeklyPlayReferenceHours
                )
            }.collect { updatedState ->
                _uiState.value = updatedState
            }
        }
    }

    fun onCategorySelect(category: EntertainmentCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onQuickDurationSelect(minutes: Int) {
        _uiState.update {
            it.copy(
                selectedDurationMinutes = minutes,
                isCustomDuration = false,
                customMinutesText = ""
            )
        }
    }

    fun onCustomDurationChange(text: String) {
        val filtered = text.filter { it.isDigit() }.take(4)
        val minutes = filtered.toIntOrNull() ?: 0
        _uiState.update {
            it.copy(
                isCustomDuration = true,
                customMinutesText = filtered,
                selectedDurationMinutes = minutes
            )
        }
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun onIntentionalSelect(intentional: Boolean?) {
        _uiState.update {
            val nextValue = if (it.isIntentional == intentional) null else intentional
            it.copy(isIntentional = nextValue)
        }
    }

    fun onDateChange(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun updateWeeklyReferenceHours(hours: Int?) {
        viewModelScope.launch {
            settingsRepository.updateWeeklyPlayReferenceHours(hours)
        }
    }

    fun logActivity() {
        val currentState = _uiState.value
        val minutes = if (currentState.isCustomDuration) {
            currentState.customMinutesText.toIntOrNull() ?: 0
        } else {
            currentState.selectedDurationMinutes
        }

        if (minutes <= 0) {
            _uiState.update { it.copy(message = "Please enter a valid duration") }
            return
        }

        viewModelScope.launch {
            val log = EntertainmentLog(
                category = currentState.selectedCategory,
                date = currentState.selectedDate,
                durationMinutes = minutes,
                note = currentState.note.trim().ifBlank { null },
                isIntentional = currentState.isIntentional
            )
            entertainmentRepository.insertLog(log)
            _uiState.update {
                it.copy(
                    note = "",
                    customMinutesText = "",
                    isCustomDuration = false,
                    selectedDurationMinutes = 30,
                    isIntentional = null,
                    message = getPermissionGivingMessage(minutes)
                )
            }
        }
    }

    private fun getPermissionGivingMessage(durationMinutes: Int): String {
        val durText = if (durationMinutes >= 60) {
            val h = durationMinutes / 60
            val m = durationMinutes % 60
            if (m > 0) "${h}h ${m}m" else "${h}h"
        } else {
            "${durationMinutes}m"
        }
        val messages = listOf(
            "Logged. Enjoy your $durText, guilt-free.",
            "Noted — go enjoy your $durText.",
            "Time well spent. Enjoy your $durText of recharge.",
            "Rest is part of the process. Enjoy your $durText!",
            "Logged! Go enjoy your $durText with presence."
        )
        return messages.random()
    }

    fun deleteLog(id: Long) {
        viewModelScope.launch {
            entertainmentRepository.deleteLogById(id)
            _uiState.update { it.copy(message = "Entry removed") }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}

