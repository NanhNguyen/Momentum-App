package com.momentum.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ─────────────────────────── Task DAO ───────────────────────────

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY dateEpochDay DESC, createdAtEpochMilli DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dateEpochDay = :epochDay ORDER BY isBigThree DESC, priority DESC")
    fun getTasksForDate(epochDay: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dateEpochDay = :epochDay AND isBigThree = 1 ORDER BY createdAtEpochMilli ASC")
    fun getBigThreeForDate(epochDay: Long): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE dateEpochDay = :epochDay AND isBigThree = 1")
    suspend fun getBigThreeCountForDate(epochDay: Long): Int

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    // For Weekly Insights
    @Query("""
        SELECT * FROM tasks 
        WHERE dateEpochDay >= :startEpochDay AND dateEpochDay <= :endEpochDay
    """)
    fun getTasksForWeek(startEpochDay: Long, endEpochDay: Long): Flow<List<TaskEntity>>

    // For Recurring Tasks
    @Query("SELECT * FROM tasks WHERE isRecurring = 1")
    fun getRecurringTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isRecurring = 1")
    suspend fun getRecurringTasksSync(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE title = :title AND dateEpochDay = :epochDay LIMIT 1")
    suspend fun getTaskByTitleAndDate(title: String, epochDay: Long): TaskEntity?

    @Query("DELETE FROM tasks WHERE title = :title AND isRecurring = 1 AND dateEpochDay >= :fromEpochDay")
    suspend fun deleteFutureRecurringTasks(title: String, fromEpochDay: Long)

    @Query("""
        UPDATE tasks 
        SET title = :newTitle, description = :newDescription, priority = :newPriority, recurrenceRule = :newRule 
        WHERE title = :oldTitle AND isRecurring = 1 AND dateEpochDay >= :fromEpochDay
    """)
    suspend fun updateFutureRecurringTasks(
        oldTitle: String,
        newTitle: String,
        newDescription: String?,
        newPriority: Int,
        newRule: String?,
        fromEpochDay: Long
    )
}

// ─────────────────────────── Habit DAO ───────────────────────────

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAtEpochMilli ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Long): HabitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Long)
}

// ─────────────────────────── HabitLog DAO ───────────────────────────

@Dao
interface HabitLogDao {
    @Query("SELECT * FROM habit_logs")
    fun getAllLogs(): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY dateEpochDay ASC")
    fun getLogsForHabit(habitId: Long): Flow<List<HabitLogEntity>>

    @Query("""
        SELECT * FROM habit_logs 
        WHERE habitId = :habitId AND dateEpochDay >= :startDay AND dateEpochDay <= :endDay
    """)
    fun getLogsForHabitInRange(habitId: Long, startDay: Long, endDay: Long): Flow<List<HabitLogEntity>>

    @Query("""
        SELECT * FROM habit_logs 
        WHERE dateEpochDay >= :startDay AND dateEpochDay <= :endDay
    """)
    fun getAllLogsForWeek(startDay: Long, endDay: Long): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND dateEpochDay = :epochDay LIMIT 1")
    suspend fun getLogForHabitOnDate(habitId: Long, epochDay: Long): HabitLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLogEntity): Long

    @Update
    suspend fun updateLog(log: HabitLogEntity)

    /** Upsert: update if a log already exists for this habit+date, otherwise insert. */
    @Transaction
    suspend fun upsertLog(log: HabitLogEntity) {
        val existing = getLogForHabitOnDate(log.habitId, log.dateEpochDay)
        if (existing != null) {
            updateLog(log.copy(id = existing.id))
        } else {
            insertLog(log)
        }
    }

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId")
    suspend fun deleteLogsForHabit(habitId: Long)
}

// ─────────────────────────── Reflection DAO ───────────────────────────

@Dao
interface ReflectionDao {
    @Query("SELECT * FROM reflections ORDER BY dateEpochDay DESC")
    fun getAllReflections(): Flow<List<ReflectionEntity>>

    @Query("SELECT * FROM reflections WHERE dateEpochDay = :epochDay LIMIT 1")
    fun getReflectionForDate(epochDay: Long): Flow<ReflectionEntity?>

    @Query("SELECT * FROM reflections WHERE dateEpochDay = :epochDay LIMIT 1")
    suspend fun getReflectionForDateSync(epochDay: Long): ReflectionEntity?

    @Query("""
        SELECT * FROM reflections 
        WHERE dateEpochDay >= :startDay AND dateEpochDay <= :endDay 
        ORDER BY dateEpochDay ASC
    """)
    fun getReflectionsForWeek(startDay: Long, endDay: Long): Flow<List<ReflectionEntity>>

    /** Upsert: one reflection per day. Replaces if date already exists. */
    @Transaction
    suspend fun upsertReflection(entry: ReflectionEntity) {
        val existing = getReflectionForDateSync(entry.dateEpochDay)
        if (existing != null) {
            insertReflection(entry.copy(id = existing.id))
        } else {
            insertReflection(entry)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(entry: ReflectionEntity): Long

    @Delete
    suspend fun deleteReflection(entry: ReflectionEntity)

    @Query("DELETE FROM reflections WHERE dateEpochDay = :epochDay")
    suspend fun deleteReflectionByDate(epochDay: Long)
}

// ─────────────────────────── Entertainment DAO ───────────────────────────

@Dao
interface EntertainmentDao {
    @Query("SELECT * FROM entertainment_logs ORDER BY dateEpochDay DESC, createdAtEpochMilli DESC")
    fun getAllLogs(): Flow<List<EntertainmentLogEntity>>

    @Query("SELECT * FROM entertainment_logs WHERE dateEpochDay = :epochDay ORDER BY createdAtEpochMilli ASC")
    fun getLogsForDate(epochDay: Long): Flow<List<EntertainmentLogEntity>>

    @Query("SELECT * FROM entertainment_logs WHERE dateEpochDay >= :startDay AND dateEpochDay <= :endDay ORDER BY dateEpochDay ASC")
    fun getLogsForWeek(startDay: Long, endDay: Long): Flow<List<EntertainmentLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: EntertainmentLogEntity): Long

    @Update
    suspend fun updateLog(log: EntertainmentLogEntity)

    @Delete
    suspend fun deleteLog(log: EntertainmentLogEntity)

    @Query("DELETE FROM entertainment_logs WHERE id = :id")
    suspend fun deleteLogById(id: Long)
}

// ─────────────────────────── AppSettings DAO ───────────────────────────

@Dao
interface AppSettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<AppSettingsEntity?>

    @Query("SELECT * FROM app_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsSync(): AppSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSettings(settings: AppSettingsEntity)
}
