package com.momentum.app.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momentum.app.data.repository.HabitRepository
import com.momentum.app.data.repository.TaskRepository
import com.momentum.app.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TodayUiState(
    val greeting: String = "",
    val bigThree: List<Task> = emptyList(),
    val otherTasks: List<Task> = emptyList(),
    val todayHabits: List<Pair<Habit, HabitLog?>> = emptyList(),
    val bigThreeCompleted: Int = 0,
    val bigThreeTotal: Int = 0,
    val showBigThreeLimitMessage: Boolean = false,
    val entertainmentMinutesToday: Int = 0,
    val restMinutesToday: Int = 0
)

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val habitRepository: HabitRepository,
    private val entertainmentRepository: com.momentum.app.data.repository.EntertainmentRepository
) : ViewModel() {

    private val today: LocalDate get() = LocalDate.now()
    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(greeting = buildGreeting()) }
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Generate recurring tasks for today if not already generated
            taskRepository.generateRecurringTasksForDate(today)

            // Combine big three tasks + habits + entertainment logs for today
            combine(
                taskRepository.getBigThreeForDate(today),
                taskRepository.getTasksForDate(today),
                habitRepository.getAllHabits(),
                habitRepository.getAllLogsForWeek(today, today),
                entertainmentRepository.getLogsForDate(today)
            ) { bigThree, allTasks, habits, logsToday, entertainmentLogsToday ->
                val otherTasks = allTasks.filter { !it.isBigThree }
                val habitPairs = habits.map { habit ->
                    val log = logsToday.firstOrNull { it.habitId == habit.id }
                    habit to log
                }
                val entMinutes = entertainmentLogsToday
                    .filter { it.category != EntertainmentCategory.REST }
                    .sumOf { it.durationMinutes }
                val restMinutes = entertainmentLogsToday
                    .filter { it.category == EntertainmentCategory.REST }
                    .sumOf { it.durationMinutes }

                TodayUiState(
                    greeting = buildGreeting(),
                    bigThree = bigThree,
                    otherTasks = otherTasks,
                    todayHabits = habitPairs,
                    bigThreeCompleted = bigThree.count { it.isCompleted },
                    bigThreeTotal = bigThree.size,
                    showBigThreeLimitMessage = false,
                    entertainmentMinutesToday = entMinutes,
                    restMinutesToday = restMinutes
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun toggleTaskComplete(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun toggleHabitForToday(habit: Habit, currentLog: HabitLog?) {
        viewModelScope.launch {
            val newStatus = when (currentLog?.status) {
                HabitStatus.DONE -> HabitStatus.MISSED
                HabitStatus.MISSED -> HabitStatus.NOT_SCHEDULED
                else -> HabitStatus.DONE
            }
            val log = HabitLog(
                id = currentLog?.id ?: 0L,
                habitId = habit.id,
                date = today,
                status = newStatus
            )
            habitRepository.upsertLog(log)
        }
    }

    fun dismissBigThreeLimitMessage() {
        _uiState.update { it.copy(showBigThreeLimitMessage = false) }
    }

    private fun buildGreeting(): String {
        val hour = java.time.LocalTime.now().hour
        return when {
            hour < 12 -> "Good morning"
            hour < 17 -> "Good afternoon"
            else -> "Good evening"
        }
    }
}
