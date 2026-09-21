package com.momentum.app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.momentum.app.domain.model.HabitStatus
import com.momentum.app.domain.model.Mood

/**
 * Room entity for Task.
 * LocalDate and Instant are stored as Long (epoch day / epoch millis).
 */
@Entity(
    tableName = "tasks",
    indices = [Index(value = ["dateEpochDay"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String?,
    val dateEpochDay: Long,          // LocalDate.toEpochDay()
    val isBigThree: Boolean,
    val isCompleted: Boolean,
    val priority: Int,
    val createdAtEpochMilli: Long,   // Instant.toEpochMilli()
    val isRecurring: Boolean = false,
    val recurrenceRule: String? = null // RecurrenceRule.name or null
)

/**
 * Room entity for Habit.
 */
@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String?,
    val frequencyPerWeek: Int,
    val createdAtEpochMilli: Long
)

/**
 * Room entity for HabitLog.
 * Each row represents one habit on one day.
 * Unique constraint prevents duplicate logs for same habit on same day.
 * Foreign key cascades deletions when parent habit is removed.
 */
@Entity(
    tableName = "habit_logs",
    indices = [Index(value = ["habitId", "dateEpochDay"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = HabitEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HabitLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val dateEpochDay: Long,
    val status: String               // HabitStatus.name()
)

/**
 * Room entity for ReflectionEntry.
 * Only one reflection per day — enforced by unique index and upsert.
 */
@Entity(
    tableName = "reflections",
    indices = [Index(value = ["dateEpochDay"], unique = true)]
)
data class ReflectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateEpochDay: Long,
    val wentWell: String?,
    val distracted: String?,
    val improveTomorrow: String?,
    val mood: String,                // Mood.name()
    val energyLevel: Int
)

/**
 * Room entity for EntertainmentLog.
 */
@Entity(
    tableName = "entertainment_logs",
    indices = [Index(value = ["dateEpochDay"])]
)
data class EntertainmentLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,            // EntertainmentCategory.name
    val dateEpochDay: Long,          // LocalDate.toEpochDay()
    val durationMinutes: Int,
    val note: String?,
    val createdAtEpochMilli: Long    // Instant.toEpochMilli()
)

/**
 * Room entity for AppSettings (Singleton row with id = 1).
 */
@Entity(tableName = "app_settings")
data class AppSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val isOnboardingCompleted: Boolean = false,
    val isEveningReminderEnabled: Boolean = true,
    val eveningReminderHour: Int = 21,
    val eveningReminderMinute: Int = 0
)
