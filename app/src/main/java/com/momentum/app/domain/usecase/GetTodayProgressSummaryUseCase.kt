package com.momentum.app.domain.usecase

import com.momentum.app.data.repository.EntertainmentRepository
import com.momentum.app.data.repository.HabitRepository
import com.momentum.app.data.repository.TaskRepository
import com.momentum.app.domain.model.HabitStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

data class TodayProgressSummary(
    val bigThreeCompleted: Int = 0,
    val bigThreeTotal: Int = 0,
    val habitsCompleted: Int = 0,
    val totalLeisureMinutes: Int = 0
) {
    fun toDisplayText(): String {
        val b3Text = if (bigThreeTotal > 0) {
            "$bigThreeCompleted/$bigThreeTotal Big 3 done"
        } else {
            "0/3 Big 3 done"
        }
        val habitText = if (habitsCompleted == 1) {
            "1 habit checked in"
        } else {
            "$habitsCompleted habits checked in"
        }
        val leisureText = if (totalLeisureMinutes >= 60) {
            val h = totalLeisureMinutes / 60
            val m = totalLeisureMinutes % 60
            if (m > 0) "${h}h ${m}m logged" else "${h}h logged"
        } else {
            "${totalLeisureMinutes}m logged"
        }
        return "Today: $b3Text · $habitText · $leisureText"
    }
}

@Singleton
class GetTodayProgressSummaryUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val habitRepository: HabitRepository,
    private val entertainmentRepository: EntertainmentRepository
) {
    operator fun invoke(date: LocalDate = LocalDate.now()): Flow<TodayProgressSummary> {
        return combine(
            taskRepository.getBigThreeForDate(date),
            habitRepository.getAllLogsForWeek(date, date),
            entertainmentRepository.getLogsForDate(date)
        ) { b3Tasks, habitLogs, leisureLogs ->
            val b3Completed = b3Tasks.count { it.isCompleted }
            val b3Total = b3Tasks.size
            val habitsDone = habitLogs.count { it.status == HabitStatus.DONE }
            val leisureMins = leisureLogs.sumOf { it.durationMinutes }
            TodayProgressSummary(
                bigThreeCompleted = b3Completed,
                bigThreeTotal = b3Total,
                habitsCompleted = habitsDone,
                totalLeisureMinutes = leisureMins
            )
        }
    }
}
