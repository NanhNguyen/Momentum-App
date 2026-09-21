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
    val restMinutesToday: Int = 0,
    // "This week at a glance"
    val weekConsistencyScore: Int = 0,
    val weekTasksCompleted: Int = 0,
    val weekBigThreeRate: Int = 0
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

            val monday = today.with(java.time.DayOfWeek.MONDAY)
            val sunday = today.with(java.time.DayOfWeek.SUNDAY)

            val todayTasksFlow = combine(
                taskRepository.getBigThreeForDate(today),
                taskRepository.getTasksForDate(today)
            ) { b3, all -> b3 to all }

            val todayOtherFlow = combine(
                habitRepository.getAllHabits(),
                habitRepository.getAllLogsForWeek(today, today),
                entertainmentRepository.getLogsForDate(today)
            ) { habits, logs, ent -> Triple(habits, logs, ent) }

            val weekDataFlow = combine(
                taskRepository.getTasksForWeek(monday, sunday),
                habitRepository.getAllLogsForWeek(monday, sunday)
            ) { weekTasks, weekLogs -> weekTasks to weekLogs }

            combine(todayTasksFlow, todayOtherFlow, weekDataFlow) {
                (bigThree, allTasks),
                (habits, logsToday, entertainmentLogsToday),
                (weekTasks, weekLogs) ->

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

                // Calculate weekly at a glance
                val weekTasksDone = weekTasks.count { it.isCompleted }
                val weekB3 = weekTasks.filter { it.isBigThree }
                val weekB3Rate = if (weekB3.isNotEmpty()) (weekB3.count { it.isCompleted } * 100 / weekB3.size) else 0

                val weekConsistency = if (habits.isNotEmpty()) {
                    val scores = habits.map { habit ->
                        val logsForHabit = weekLogs.filter { it.habitId == habit.id }
                        val completedDays = logsForHabit.count { it.status == HabitStatus.DONE }
                        val target = habit.frequencyPerWeek.coerceAtLeast(1)
                        ((completedDays.toFloat() / target) * 100f).toInt().coerceIn(0, 100)
                    }
                    scores.average().toInt()
                } else 0

                TodayUiState(
                    greeting = buildGreeting(),
                    bigThree = bigThree,
                    otherTasks = otherTasks,
                    todayHabits = habitPairs,
                    bigThreeCompleted = bigThree.count { it.isCompleted },
                    bigThreeTotal = bigThree.size,
                    showBigThreeLimitMessage = false,
                    entertainmentMinutesToday = entMinutes,
                    restMinutesToday = restMinutes,
                    weekConsistencyScore = weekConsistency,
                    weekTasksCompleted = weekTasksDone,
                    weekBigThreeRate = weekB3Rate
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
