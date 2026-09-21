package com.momentum.app.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SentimentNeutral
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material.icons.outlined.SentimentVeryDissatisfied
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.ui.graphics.vector.ImageVector
import java.time.Instant
import java.time.LocalDate

/**
 * Recurrence options for tasks.
 * Kept intentionally simple: Daily or Weekdays (Mon-Fri).
 */
enum class RecurrenceRule { DAILY, WEEKDAYS }

/**
 * Represents a task in Momentum.
 * @param isBigThree If true, this is one of the user's "Big 3" focus tasks for [date].
 *                   At most 3 tasks per day can be Big Three.
 * @param priority 0 = Low, 1 = Medium, 2 = High
 * @param isRecurring Whether this task repeats automatically.
 * @param recurrenceRule The repetition rule (Daily or Weekdays).
 */
data class Task(
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val date: LocalDate,
    val isBigThree: Boolean = false,
    val isCompleted: Boolean = false,
    val priority: Int = 0, // 0=Low, 1=Medium, 2=High
    val createdAt: Instant = Instant.now(),
    val isRecurring: Boolean = false,
    val recurrenceRule: RecurrenceRule? = null
)

/**
 * A habit the user wants to build (e.g., "Meditate 10 minutes").
 * @param frequencyPerWeek How many times per week this habit should be done (1–7).
 */
data class Habit(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val frequencyPerWeek: Int = 7,
    val createdAt: Instant = Instant.now()
)

/**
 * Records whether a habit was done, missed, or not scheduled on a given day.
 * Consistency Score = (DONE count / scheduled days this week) × 100%.
 * We deliberately avoid streak counters — consistency over pressure.
 */
enum class HabitStatus { DONE, MISSED, NOT_SCHEDULED }

data class HabitLog(
    val id: Long = 0,
    val habitId: Long,
    val date: LocalDate,
    val status: HabitStatus = HabitStatus.NOT_SCHEDULED
)



/**
 * Mood levels — used for daily reflection and weekly mood average.
 * Naming is neutral; never "Bad" or "Failed."
 */
enum class Mood(val icon: ImageVector, val label: String) {
    DIFFICULT(Icons.Outlined.SentimentVeryDissatisfied, "Tough day"),
    NEUTRAL(Icons.Outlined.SentimentNeutral, "Okay"),
    GOOD(Icons.Outlined.SentimentSatisfied, "Good"),
    GREAT(Icons.Outlined.SentimentVerySatisfied, "Great")
}

/**
 * A user's end-of-day reflection entry.
 * Only one entry allowed per [date]; editing is supported.
 * @param energyLevel 1 (low) to 5 (high)
 */
data class ReflectionEntry(
    val id: Long = 0,
    val date: LocalDate,
    val wentWell: String? = null,
    val distracted: String? = null,
    val improveTomorrow: String? = null,
    val mood: Mood = Mood.NEUTRAL,
    val energyLevel: Int = 3 // 1-5
)

/**
 * Categories of intentional entertainment and rest.
 * Momentum philosophy: "Don't eliminate entertainment. Make it intentional."
 */
enum class EntertainmentCategory(val label: String) {
    GAMING("Gaming"),
    YOUTUBE("YouTube"),
    NETFLIX("Netflix / Shows"),
    SOCIAL_MEDIA("Social Media"),
    OTHER("Other"),
    REST("Rest & Unwind")
}

/**
 * Log of intentional entertainment or rest time.
 */
data class EntertainmentLog(
    val id: Long = 0,
    val category: EntertainmentCategory,
    val date: LocalDate,
    val durationMinutes: Int,
    val note: String? = null,
    val createdAt: Instant = Instant.now()
)

/**
 * Application settings (onboarding completion, evening reminder).
 */
data class AppSettings(
    val id: Int = 1,
    val isOnboardingCompleted: Boolean = false,
    val isEveningReminderEnabled: Boolean = true,
    val eveningReminderHour: Int = 21,
    val eveningReminderMinute: Int = 0
)
