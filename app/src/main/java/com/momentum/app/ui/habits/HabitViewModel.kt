package com.momentum.app.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momentum.app.data.repository.HabitRepository
import com.momentum.app.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

/**
 * A habit paired with its 7 daily logs for the current week (Mon–Sun).
 * [consistencyScore] = DONE count / scheduled days this week * 100%.
 *
 * Scheduling assumption: For a habit with frequencyPerWeek = N,
 * the first N weekdays of the current Mon–Sun window are "scheduled" days.
 * This is a simple heuristic — see code comment in HabitViewModel.
 */
data class HabitWithWeekData(
    val habit: Habit,
    val weekLogs: Map<DayOfWeek, HabitLog?>,  // Mon–Sun, null = no log yet
    val consistencyScore: Int                   // 0–100
)

data class HabitsUiState(
    val habitsWithData: List<HabitWithWeekData> = emptyList(),
    val overallConsistency: Int = 0,
    val isLoading: Boolean = true
)

data class AddEditHabitUiState(
    val name: String = "",
    val description: String = "",
    val frequencyPerWeek: Int = 7,
    val isEditMode: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitsUiState())
    val uiState: StateFlow<HabitsUiState> = _uiState.asStateFlow()

    private val today: LocalDate get() = LocalDate.now()
    private val monday: LocalDate get() = today.with(DayOfWeek.MONDAY)
    private val sunday: LocalDate get() = today.with(DayOfWeek.SUNDAY)

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                repository.getAllHabits(),
                repository.getAllLogsForWeek(monday, sunday)
            ) { habits, allLogs ->
                val habitsWithData = habits.map { habit ->
                    val weekLogs = buildWeekLogMap(habit.id, allLogs)
                    val score = calculateConsistency(habit, weekLogs)
                    HabitWithWeekData(habit, weekLogs, score)
                }
                val overall = if (habitsWithData.isEmpty()) 0
                else habitsWithData.map { it.consistencyScore }.average().toInt()

                HabitsUiState(
                    habitsWithData = habitsWithData,
                    overallConsistency = overall,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun buildWeekLogMap(
        habitId: Long,
        allLogs: List<HabitLog>
    ): Map<DayOfWeek, HabitLog?> {
        val logsForHabit = allLogs.filter { it.habitId == habitId }
            .associateBy { it.date.dayOfWeek }
        return DayOfWeek.entries.associateWith { day -> logsForHabit[day] }
    }

    /**
     * Consistency Score = (DONE logs / frequencyPerWeek) * 100.
     * Evaluates total weekly completions against user target, without assuming fixed weekdays.
     */
    private fun calculateConsistency(
        habit: Habit,
        weekLogs: Map<DayOfWeek, HabitLog?>
    ): Int {
        val freq = habit.frequencyPerWeek.coerceIn(1, 7)
        val doneDays = weekLogs.values.count { it?.status == HabitStatus.DONE }
        return ((doneDays.toFloat() / freq.toFloat()) * 100).toInt().coerceIn(0, 100)
    }

    fun toggleHabit(habit: Habit, day: DayOfWeek, currentLog: HabitLog?) {
        viewModelScope.launch {
            val date = monday.with(day)
            // Only allow toggling days that have already occurred
            if (date.isAfter(today)) return@launch
            val newStatus = when (currentLog?.status) {
                HabitStatus.DONE -> HabitStatus.MISSED
                HabitStatus.MISSED -> HabitStatus.NOT_SCHEDULED
                else -> HabitStatus.DONE
            }
            val log = HabitLog(
                id = currentLog?.id ?: 0L,
                habitId = habit.id,
                date = date,
                status = newStatus
            )
            repository.upsertLog(log)
        }
    }

    fun deleteHabit(habitId: Long) {
        viewModelScope.launch {
            repository.deleteHabitById(habitId)
        }
    }
}

@HiltViewModel
class AddEditHabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditHabitUiState())
    val uiState: StateFlow<AddEditHabitUiState> = _uiState.asStateFlow()

    private var editingHabitId: Long? = null

    fun loadHabit(habitId: Long) {
        viewModelScope.launch {
            val habit = repository.getHabitById(habitId) ?: return@launch
            editingHabitId = habitId
            _uiState.update {
                it.copy(
                    name = habit.name,
                    description = habit.description ?: "",
                    frequencyPerWeek = habit.frequencyPerWeek,
                    isEditMode = true
                )
            }
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }
    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }
    fun onFrequencyChange(value: Int) = _uiState.update { it.copy(frequencyPerWeek = value.coerceIn(1, 7)) }

    fun saveHabit() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please give your habit a name.") }
            return
        }
        viewModelScope.launch {
            val existing = if (editingHabitId != null) repository.getHabitById(editingHabitId!!) else null
            val habit = Habit(
                id = editingHabitId ?: 0L,
                name = state.name.trim(),
                description = state.description.trim().ifBlank { null },
                frequencyPerWeek = state.frequencyPerWeek,
                createdAt = existing?.createdAt ?: java.time.Instant.now()
            )
            if (editingHabitId != null) repository.updateHabit(habit)
            else repository.insertHabit(habit)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
}
