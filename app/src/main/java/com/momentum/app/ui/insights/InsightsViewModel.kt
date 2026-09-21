package com.momentum.app.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momentum.app.data.repository.HabitRepository
import com.momentum.app.data.repository.ReflectionRepository
import com.momentum.app.data.repository.TaskRepository
import com.momentum.app.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

enum class InsightsTab { WEEKLY, MONTHLY }

data class WeeklyScore(val label: String, val scorePercent: Int)

data class MoodCount(val mood: Mood, val count: Int, val percentage: Int)

data class MonthlyInsightsUiState(
    val monthLabel: String = "",
    val tasksCompletedMonth: Int = 0,
    val tasksTotalMonth: Int = 0,
    val habitConsistencyMonth: Int = 0,
    val weeklyConsistencyTrend: List<WeeklyScore> = emptyList(),
    val moodDistribution: List<MoodCount> = emptyList(),
    val totalReflectionsCount: Int = 0,
    val correlationInsight: String = "",
    val hasEnoughDataForCorrelation: Boolean = false
)

data class InsightsUiState(
    val selectedTab: InsightsTab = InsightsTab.WEEKLY,
    val weekLabel: String = "",
    val tasksCompleted: Int = 0,
    val tasksTotal: Int = 0,
    val bigThreeDaysCompleted: Int = 0,  // how many days all Big 3 were done
    val habitConsistencyPercent: Int = 0,
    val averageMood: Mood? = null,
    val insights: List<String> = emptyList(),
    val monthlyState: MonthlyInsightsUiState = MonthlyInsightsUiState(),
    val isLoading: Boolean = true
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val habitRepository: HabitRepository,
    private val reflectionRepository: ReflectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    private val today: LocalDate get() = LocalDate.now()
    private val monday: LocalDate get() = today.with(DayOfWeek.MONDAY)
    private val sunday: LocalDate get() = today.with(DayOfWeek.SUNDAY)
    private val monthStart: LocalDate get() = today.minusDays(27)

    init {
        _uiState.update { it.copy(weekLabel = buildWeekLabel()) }
        loadInsights()
    }

    fun selectTab(tab: InsightsTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    private fun loadInsights() {
        viewModelScope.launch {
            // Query 4-week window to cover both current week and monthly review
            combine(
                taskRepository.getTasksForWeek(monthStart, sunday),
                habitRepository.getAllHabits(),
                habitRepository.getAllLogsForWeek(monthStart, sunday),
                reflectionRepository.getReflectionsForWeek(monthStart, sunday)
            ) { tasks4w, habits, logs4w, reflections4w ->

                // ── Weekly Calculations ──
                val tasksThisWeek = tasks4w.filter { it.date in monday..sunday }
                val logsThisWeek = logs4w.filter { it.date in monday..sunday }
                val reflectionsThisWeek = reflections4w.filter { it.date in monday..sunday }

                val tasksCompleted = tasksThisWeek.count { it.isCompleted }
                val tasksTotal = tasksThisWeek.size

                val bigThreeDaysCompleted = (0..6L).count { offset ->
                    val day = monday.plusDays(offset)
                    if (day.isAfter(today)) return@count false
                    val bigThree = tasksThisWeek.filter { it.date == day && it.isBigThree }
                    bigThree.isNotEmpty() && bigThree.all { it.isCompleted }
                }

                val habitScores = habits.map { habit ->
                    val freq = habit.frequencyPerWeek.coerceIn(1, 7)
                    val doneCount = logsThisWeek.count { log ->
                        log.habitId == habit.id && log.status == HabitStatus.DONE
                    }
                    ((doneCount.toFloat() / freq.toFloat()) * 100).toInt().coerceIn(0, 100)
                }
                val overallHabitScore = if (habitScores.isEmpty()) 0 else habitScores.average().toInt()

                val moodOrdinals = reflectionsThisWeek.map { it.mood.ordinal }
                val avgMood = if (moodOrdinals.isEmpty()) null
                else {
                    val avg = moodOrdinals.average().toInt().coerceIn(0, 3)
                    Mood.entries[avg]
                }

                val insights = buildInsights(
                    tasksThisWeek, habits, logsThisWeek, reflectionsThisWeek, monday, today
                )

                // ── Monthly Calculations (4-Week Window) ──
                val tasksCompletedMonth = tasks4w.count { it.isCompleted && it.date in monthStart..today }
                val tasksTotalMonth = tasks4w.count { it.date in monthStart..today }

                // 4-Week Trend
                val weeklyTrend = (0..3).map { w ->
                    val wStart = monthStart.plusDays(w * 7L)
                    val wEnd = wStart.plusDays(6)
                    val wLogs = logs4w.filter { it.date in wStart..wEnd }
                    val wScore = if (habits.isEmpty()) 0 else {
                        habits.map { h ->
                            val doneCount = wLogs.count { it.habitId == h.id && it.status == HabitStatus.DONE }
                            ((doneCount.toFloat() / h.frequencyPerWeek.coerceIn(1, 7).toFloat()) * 100).toInt().coerceIn(0, 100)
                        }.average().toInt()
                    }
                    val label = if (w == 3) "This Wk" else "W${w + 1}"
                    WeeklyScore(label, wScore)
                }

                val monthHabitScore = if (weeklyTrend.isEmpty()) 0 else weeklyTrend.map { it.scorePercent }.average().toInt()

                // Monthly Mood Distribution (Past 28 days)
                val reflectionsMonth = reflections4w.filter { it.date in monthStart..today }
                val totalReflections = reflectionsMonth.size
                val moodDistribution = Mood.entries.map { mood ->
                    val count = reflectionsMonth.count { it.mood == mood }
                    val pct = if (totalReflections > 0) (count * 100) / totalReflections else 0
                    MoodCount(mood, count, pct)
                }

                // Correlation Insight (Habit vs Mood)
                val hasEnoughData = totalReflections >= 7
                val correlationText = if (hasEnoughData) {
                    val habitDoneDates = logs4w.filter { it.status == HabitStatus.DONE && it.date in monthStart..today }
                        .map { it.date }.toSet()

                    val moodScoresOnDoneDays = reflectionsMonth
                        .filter { habitDoneDates.contains(it.date) }
                        .map { it.mood.ordinal + 1 } // 1..4 scale

                    val moodScoresOnOtherDays = reflectionsMonth
                        .filter { !habitDoneDates.contains(it.date) }
                        .map { it.mood.ordinal + 1 }

                    if (moodScoresOnDoneDays.isNotEmpty() && moodScoresOnOtherDays.isNotEmpty()) {
                        val avgDone = moodScoresOnDoneDays.average()
                        val avgOther = moodScoresOnOtherDays.average()
                        if (avgDone > avgOther) {
                            val diffPct = (((avgDone - avgOther) / avgOther) * 100).toInt().coerceIn(5, 100)
                            "On days you completed your habits, your mood was $diffPct% more positive. Small daily actions make a noticeable difference."
                        } else {
                            "Your mood remains steady across all days — consistency in how you feel is a quiet superpower."
                        }
                    } else if (moodScoresOnDoneDays.isNotEmpty()) {
                        "You consistently showed up for your habits this month, keeping your energy anchored."
                    } else {
                        "Keep checking in daily — consistency builds momentum."
                    }
                } else {
                    "Not enough data yet for monthly correlations. Keep checking in — insights will appear as your momentum builds."
                }

                val monthLabelStr = "${monthStart.format(DateTimeFormatter.ofPattern("MMM d"))} – ${today.format(DateTimeFormatter.ofPattern("MMM d"))}"

                val monthlyState = MonthlyInsightsUiState(
                    monthLabel = monthLabelStr,
                    tasksCompletedMonth = tasksCompletedMonth,
                    tasksTotalMonth = tasksTotalMonth,
                    habitConsistencyMonth = monthHabitScore,
                    weeklyConsistencyTrend = weeklyTrend,
                    moodDistribution = moodDistribution,
                    totalReflectionsCount = totalReflections,
                    correlationInsight = correlationText,
                    hasEnoughDataForCorrelation = hasEnoughData
                )

                _uiState.value.copy(
                    weekLabel = buildWeekLabel(),
                    tasksCompleted = tasksCompleted,
                    tasksTotal = tasksTotal,
                    bigThreeDaysCompleted = bigThreeDaysCompleted,
                    habitConsistencyPercent = overallHabitScore,
                    averageMood = avgMood,
                    insights = insights,
                    monthlyState = monthlyState,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    private fun buildInsights(
        tasks: List<Task>,
        habits: List<Habit>,
        logs: List<HabitLog>,
        reflections: List<ReflectionEntry>,
        monday: LocalDate,
        today: LocalDate
    ): List<String> {
        val results = mutableListOf<String>()

        // Best habit insight
        if (habits.isNotEmpty()) {
            val bestHabit = habits.maxByOrNull { habit ->
                logs.count { it.habitId == habit.id && it.status == HabitStatus.DONE }
            }
            bestHabit?.let { h ->
                val doneDays = logs.count { it.habitId == h.id && it.status == HabitStatus.DONE }
                if (doneDays > 0) {
                    results.add("\"${h.name}\" is your strongest habit this week — keep going!")
                }
            }
        }

        // Most consistent day of week
        val dayDoneCounts = DayOfWeek.entries.associateWith { day ->
            logs.count { log ->
                log.date.dayOfWeek == day && log.status == HabitStatus.DONE
            }
        }
        val bestDay = dayDoneCounts.maxByOrNull { it.value }
        if ((bestDay?.value ?: 0) > 0) {
            val dayName = bestDay!!.key.getDisplayName(TextStyle.FULL, Locale.getDefault())
            results.add("You've been most consistent on ${dayName}s — a great anchor for your week.")
        }

        // Mood trend
        if (reflections.size >= 3) {
            val sorted = reflections.sortedBy { it.date }
            val firstHalf = sorted.take(sorted.size / 2).map { it.mood.ordinal }
            val secondHalf = sorted.drop(sorted.size / 2).map { it.mood.ordinal }
            val firstAvg = firstHalf.average()
            val secondAvg = secondHalf.average()
            when {
                secondAvg > firstAvg -> results.add("Your mood trended upward this week — that's a wonderful sign of progress!")
                secondAvg < firstAvg -> results.add("The week felt a bit heavier toward the end. Rest is part of the process too.")
                else -> results.add("Your mood stayed steady this week — consistency in how you feel is a quiet strength.")
            }
        }

        // Task completion
        val completedTasks = tasks.count { it.isCompleted }
        val totalTasks = tasks.size
        if (totalTasks > 0) {
            val pct = (completedTasks * 100) / totalTasks
            when {
                pct >= 80 -> results.add("You completed ${pct}% of your tasks this week — excellent focus!")
                pct >= 50 -> results.add("You completed ${pct}% of tasks this week. Progress over perfection.")
                pct < 50 && totalTasks > 5 -> results.add("Your workload may have been ambitious this week. Consider focusing on fewer tasks next week.")
            }
        }

        // Big 3 encouragement
        val bigThreeTasks = tasks.filter { it.isBigThree }
        if (bigThreeTasks.isNotEmpty()) {
            val completed = bigThreeTasks.count { it.isCompleted }
            if (completed == bigThreeTasks.size) {
                results.add("You completed every Big 3 task this week — that's what momentum looks like!")
            }
        }

        return results.take(4)
    }

    private fun buildWeekLabel(): String {
        val monStr = monday.format(DateTimeFormatter.ofPattern("MMM d"))
        val sunStr = sunday.format(DateTimeFormatter.ofPattern("MMM d"))
        return "$monStr – $sunStr"
    }
}
