package com.momentum.app.data.local

import com.momentum.app.domain.model.*
import java.time.Instant
import java.time.LocalDate

// ─────────────── Task Mappers ───────────────

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    description = description,
    date = LocalDate.ofEpochDay(dateEpochDay),
    isBigThree = isBigThree,
    isCompleted = isCompleted,
    priority = priority,
    createdAt = Instant.ofEpochMilli(createdAtEpochMilli),
    isRecurring = isRecurring,
    recurrenceRule = recurrenceRule?.let { runCatching { RecurrenceRule.valueOf(it) }.getOrNull() }
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    dateEpochDay = date.toEpochDay(),
    isBigThree = isBigThree,
    isCompleted = isCompleted,
    priority = priority,
    createdAtEpochMilli = createdAt.toEpochMilli(),
    isRecurring = isRecurring,
    recurrenceRule = recurrenceRule?.name
)

// ─────────────── Habit Mappers ───────────────

fun HabitEntity.toDomain(): Habit = Habit(
    id = id,
    name = name,
    description = description,
    frequencyPerWeek = frequencyPerWeek,
    createdAt = Instant.ofEpochMilli(createdAtEpochMilli)
)

fun Habit.toEntity(): HabitEntity = HabitEntity(
    id = id,
    name = name,
    description = description,
    frequencyPerWeek = frequencyPerWeek,
    createdAtEpochMilli = createdAt.toEpochMilli()
)

// ─────────────── HabitLog Mappers ───────────────

fun HabitLogEntity.toDomain(): HabitLog = HabitLog(
    id = id,
    habitId = habitId,
    date = LocalDate.ofEpochDay(dateEpochDay),
    status = runCatching { HabitStatus.valueOf(status) }.getOrDefault(HabitStatus.NOT_SCHEDULED)
)

fun HabitLog.toEntity(): HabitLogEntity = HabitLogEntity(
    id = id,
    habitId = habitId,
    dateEpochDay = date.toEpochDay(),
    status = status.name
)

// ─────────────── Reflection Mappers ───────────────

fun ReflectionEntity.toDomain(): ReflectionEntry = ReflectionEntry(
    id = id,
    date = LocalDate.ofEpochDay(dateEpochDay),
    wentWell = wentWell,
    distracted = distracted,
    improveTomorrow = improveTomorrow,
    mood = runCatching { Mood.valueOf(mood) }.getOrDefault(Mood.NEUTRAL),
    energyLevel = energyLevel
)

fun ReflectionEntry.toEntity(): ReflectionEntity = ReflectionEntity(
    id = id,
    dateEpochDay = date.toEpochDay(),
    wentWell = wentWell,
    distracted = distracted,
    improveTomorrow = improveTomorrow,
    mood = mood.name,
    energyLevel = energyLevel
)

// ─────────────── Entertainment Mappers ───────────────

fun EntertainmentLogEntity.toDomain(): EntertainmentLog = EntertainmentLog(
    id = id,
    category = runCatching { EntertainmentCategory.valueOf(category) }.getOrDefault(EntertainmentCategory.OTHER),
    date = LocalDate.ofEpochDay(dateEpochDay),
    durationMinutes = durationMinutes,
    note = note,
    createdAt = Instant.ofEpochMilli(createdAtEpochMilli)
)

fun EntertainmentLog.toEntity(): EntertainmentLogEntity = EntertainmentLogEntity(
    id = id,
    category = category.name,
    dateEpochDay = date.toEpochDay(),
    durationMinutes = durationMinutes,
    note = note,
    createdAtEpochMilli = createdAt.toEpochMilli()
)

// ─────────────── AppSettings Mappers ───────────────

fun AppSettingsEntity.toDomain(): AppSettings = AppSettings(
    id = id,
    isOnboardingCompleted = isOnboardingCompleted,
    isEveningReminderEnabled = isEveningReminderEnabled,
    eveningReminderHour = eveningReminderHour,
    eveningReminderMinute = eveningReminderMinute
)

fun AppSettings.toEntity(): AppSettingsEntity = AppSettingsEntity(
    id = id,
    isOnboardingCompleted = isOnboardingCompleted,
    isEveningReminderEnabled = isEveningReminderEnabled,
    eveningReminderHour = eveningReminderHour,
    eveningReminderMinute = eveningReminderMinute
)
