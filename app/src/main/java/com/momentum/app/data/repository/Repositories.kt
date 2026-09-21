package com.momentum.app.data.repository

import com.momentum.app.data.local.*
import com.momentum.app.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import androidx.room.withTransaction
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for tasks.
 * Enforces the Big Three rule: max 3 tasks marked isBigThree per day.
 */
@Singleton
class TaskRepository @Inject constructor(private val dao: TaskDao) {

    fun getAllTasks(): Flow<List<Task>> =
        dao.getAllTasks().map { list -> list.map { it.toDomain() } }

    fun getTasksForDate(date: LocalDate): Flow<List<Task>> =
        dao.getTasksForDate(date.toEpochDay()).map { list -> list.map { it.toDomain() } }

    fun getBigThreeForDate(date: LocalDate): Flow<List<Task>> =
        dao.getBigThreeForDate(date.toEpochDay()).map { list -> list.map { it.toDomain() } }

    fun getTasksForWeek(startDate: LocalDate, endDate: LocalDate): Flow<List<Task>> =
        dao.getTasksForWeek(startDate.toEpochDay(), endDate.toEpochDay())
            .map { list -> list.map { it.toDomain() } }

    /**
     * Returns the count of Big Three tasks for [date].
     * Use this before marking a task as Big Three to enforce the 3-task limit.
     */
    suspend fun getBigThreeCount(date: LocalDate): Int =
        dao.getBigThreeCountForDate(date.toEpochDay())

    suspend fun insertTask(task: Task): Long = dao.insertTask(task.toEntity())

    suspend fun updateTask(task: Task) = dao.updateTask(task.toEntity())

    suspend fun deleteTask(task: Task) = dao.deleteTask(task.toEntity())

    suspend fun deleteTaskById(id: Long) = dao.deleteTaskById(id)

    suspend fun getTaskById(id: Long): Task? = dao.getTaskById(id)?.toDomain()

    fun getRecurringTasks(): Flow<List<Task>> =
        dao.getRecurringTasks().map { list -> list.map { it.toDomain() } }

    suspend fun getRecurringTasksSync(): List<Task> =
        dao.getRecurringTasksSync().map { it.toDomain() }

    suspend fun deleteFutureRecurringTasks(title: String, fromDate: LocalDate) =
        dao.deleteFutureRecurringTasks(title, fromDate.toEpochDay())

    suspend fun updateFutureRecurringTasks(
        oldTitle: String,
        newTitle: String,
        newDescription: String?,
        newPriority: Int,
        newRule: RecurrenceRule?,
        fromDate: LocalDate
    ) = dao.updateFutureRecurringTasks(
        oldTitle = oldTitle,
        newTitle = newTitle,
        newDescription = newDescription,
        newPriority = newPriority,
        newRule = newRule?.name,
        fromEpochDay = fromDate.toEpochDay()
    )

    /**
     * Checks all recurring tasks and generates instances for [date] if they match recurrence rules
     * and do not exist yet.
     */
    suspend fun generateRecurringTasksForDate(date: LocalDate) {
        val recurringTasks = dao.getRecurringTasksSync()
        val distinctTasks = recurringTasks.distinctBy { it.title }
        distinctTasks.forEach { entity ->
            val rule = entity.recurrenceRule?.let { runCatching { RecurrenceRule.valueOf(it) }.getOrNull() }
            val shouldGenerate = when (rule) {
                RecurrenceRule.DAILY -> true
                RecurrenceRule.WEEKDAYS -> date.dayOfWeek != java.time.DayOfWeek.SATURDAY && date.dayOfWeek != java.time.DayOfWeek.SUNDAY
                null -> false
            }
            if (shouldGenerate) {
                val existing = dao.getTaskByTitleAndDate(entity.title, date.toEpochDay())
                if (existing == null) {
                    val newEntity = entity.copy(
                        id = 0L,
                        dateEpochDay = date.toEpochDay(),
                        isCompleted = false,
                        isBigThree = false,
                        createdAtEpochMilli = System.currentTimeMillis()
                    )
                    dao.insertTask(newEntity)
                }
            }
        }
    }
}

/**
 * Single source of truth for habits and their logs.
 */
@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao,
    private val logDao: HabitLogDao
) {
    fun getAllHabits(): Flow<List<Habit>> =
        habitDao.getAllHabits().map { list -> list.map { it.toDomain() } }

    suspend fun insertHabit(habit: Habit): Long = habitDao.insertHabit(habit.toEntity())

    suspend fun updateHabit(habit: Habit) = habitDao.updateHabit(habit.toEntity())

    suspend fun getHabitById(id: Long): Habit? = habitDao.getHabitById(id)?.toDomain()

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit.toEntity())
        logDao.deleteLogsForHabit(habit.id)
    }

    suspend fun deleteHabitById(id: Long) {
        habitDao.deleteHabitById(id)
        logDao.deleteLogsForHabit(id)
    }

    fun getLogsForHabit(habitId: Long): Flow<List<HabitLog>> =
        logDao.getLogsForHabit(habitId).map { list -> list.map { it.toDomain() } }

    fun getLogsForHabitInRange(
        habitId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<HabitLog>> =
        logDao.getLogsForHabitInRange(habitId, startDate.toEpochDay(), endDate.toEpochDay())
            .map { list -> list.map { it.toDomain() } }

    fun getAllLogsForWeek(startDate: LocalDate, endDate: LocalDate): Flow<List<HabitLog>> =
        logDao.getAllLogsForWeek(startDate.toEpochDay(), endDate.toEpochDay())
            .map { list -> list.map { it.toDomain() } }

    suspend fun upsertLog(log: HabitLog) = logDao.upsertLog(log.toEntity())
}

/**
 * Single source of truth for daily reflections.
 * Enforces one entry per day via upsert.
 */
@Singleton
class ReflectionRepository @Inject constructor(private val dao: ReflectionDao) {
    fun getAllReflections(): Flow<List<ReflectionEntry>> =
        dao.getAllReflections().map { list -> list.map { it.toDomain() } }

    fun getAllReflectionsFlow(): Flow<List<ReflectionEntry>> = getAllReflections()

    fun getReflectionForDate(date: LocalDate): Flow<ReflectionEntry?> =
        dao.getReflectionForDate(date.toEpochDay()).map { it?.toDomain() }

    suspend fun getReflectionByDate(date: LocalDate): ReflectionEntry? =
        dao.getReflectionForDateSync(date.toEpochDay())?.toDomain()

    fun getReflectionsForWeek(startDate: LocalDate, endDate: LocalDate): Flow<List<ReflectionEntry>> =
        dao.getReflectionsForWeek(startDate.toEpochDay(), endDate.toEpochDay())
            .map { list -> list.map { it.toDomain() } }

    suspend fun upsertReflection(entry: ReflectionEntry) = dao.upsertReflection(entry.toEntity())

    suspend fun deleteReflection(entry: ReflectionEntry) = dao.deleteReflection(entry.toEntity())

    suspend fun deleteReflectionByDate(date: LocalDate) = dao.deleteReflectionByDate(date.toEpochDay())
}

/**
 * Single source of truth for entertainment and rest tracking.
 */
@Singleton
class EntertainmentRepository @Inject constructor(private val dao: EntertainmentDao) {
    fun getAllLogs(): Flow<List<EntertainmentLog>> =
        dao.getAllLogs().map { list -> list.map { it.toDomain() } }

    fun getLogsForDate(date: LocalDate): Flow<List<EntertainmentLog>> =
        dao.getLogsForDate(date.toEpochDay()).map { list -> list.map { it.toDomain() } }

    fun getLogsForWeek(startDate: LocalDate, endDate: LocalDate): Flow<List<EntertainmentLog>> =
        dao.getLogsForWeek(startDate.toEpochDay(), endDate.toEpochDay())
            .map { list -> list.map { it.toDomain() } }

    suspend fun insertLog(log: EntertainmentLog): Long = dao.insertLog(log.toEntity())

    suspend fun updateLog(log: EntertainmentLog) = dao.updateLog(log.toEntity())

    suspend fun deleteLog(log: EntertainmentLog) = dao.deleteLog(log.toEntity())

    suspend fun deleteLogById(id: Long) = dao.deleteLogById(id)
}

/**
 * Single source of truth for user settings (onboarding & reminder).
 */
@Singleton
class SettingsRepository @Inject constructor(private val dao: AppSettingsDao) {
    fun getSettings(): Flow<AppSettings> =
        dao.getSettings().map { entity ->
            entity?.toDomain() ?: AppSettings()
        }

    suspend fun getSettingsSync(): AppSettings =
        dao.getSettingsSync()?.toDomain() ?: AppSettings()

    suspend fun updateOnboardingCompleted(completed: Boolean) {
        val current = dao.getSettingsSync() ?: AppSettingsEntity()
        dao.insertOrUpdateSettings(current.copy(isOnboardingCompleted = completed))
    }

    suspend fun updateEveningReminder(enabled: Boolean, hour: Int, minute: Int) {
        val current = dao.getSettingsSync() ?: AppSettingsEntity()
        dao.insertOrUpdateSettings(
            current.copy(
                isEveningReminderEnabled = enabled,
                eveningReminderHour = hour,
                eveningReminderMinute = minute
            )
        )
    }

    suspend fun updateWeeklyPlayReferenceHours(hours: Int?) {
        val current = dao.getSettingsSync() ?: AppSettingsEntity()
        dao.insertOrUpdateSettings(current.copy(weeklyPlayReferenceHours = hours))
    }
}

/**
 * Handles JSON export and import across all Room tables via Storage Access Framework.
 */
@Singleton
class BackupRepository @Inject constructor(
    private val database: MomentumDatabase,
    private val taskDao: TaskDao,
    private val habitDao: HabitDao,
    private val habitLogDao: HabitLogDao,
    private val reflectionDao: ReflectionDao,
    private val entertainmentDao: EntertainmentDao,
    private val appSettingsDao: AppSettingsDao
) {
    suspend fun exportBackupJson(): String = withContext(Dispatchers.IO) {
        val tasks = taskDao.getAllTasks().first()
        val habits = habitDao.getAllHabits().first()
        val habitLogs = habitLogDao.getAllLogs().first()
        val reflections = reflectionDao.getAllReflections().first()
        val entertainment = entertainmentDao.getAllLogs().first()
        val settings = appSettingsDao.getSettingsSync() ?: AppSettingsEntity()

        val root = org.json.JSONObject()
        root.put("version", 1)
        root.put("exportedAt", java.time.Instant.now().toString())

        val tasksArr = org.json.JSONArray()
        tasks.forEach { t ->
            val obj = org.json.JSONObject()
            obj.put("id", t.id)
            obj.put("title", t.title)
            obj.put("description", t.description ?: "")
            obj.put("dateEpochDay", t.dateEpochDay)
            obj.put("isBigThree", t.isBigThree)
            obj.put("isCompleted", t.isCompleted)
            obj.put("priority", t.priority)
            obj.put("createdAtEpochMilli", t.createdAtEpochMilli)
            obj.put("isRecurring", t.isRecurring)
            obj.put("recurrenceRule", t.recurrenceRule ?: "")
            tasksArr.put(obj)
        }
        root.put("tasks", tasksArr)

        val habitsArr = org.json.JSONArray()
        habits.forEach { h ->
            val obj = org.json.JSONObject()
            obj.put("id", h.id)
            obj.put("name", h.name)
            obj.put("description", h.description ?: "")
            obj.put("frequencyPerWeek", h.frequencyPerWeek)
            obj.put("createdAtEpochMilli", h.createdAtEpochMilli)
            habitsArr.put(obj)
        }
        root.put("habits", habitsArr)

        val logsArr = org.json.JSONArray()
        habitLogs.forEach { l ->
            val obj = org.json.JSONObject()
            obj.put("id", l.id)
            obj.put("habitId", l.habitId)
            obj.put("dateEpochDay", l.dateEpochDay)
            obj.put("status", l.status)
            logsArr.put(obj)
        }
        root.put("habitLogs", logsArr)

        val reflArr = org.json.JSONArray()
        reflections.forEach { r ->
            val obj = org.json.JSONObject()
            obj.put("id", r.id)
            obj.put("dateEpochDay", r.dateEpochDay)
            obj.put("wentWell", r.wentWell ?: "")
            obj.put("distracted", r.distracted ?: "")
            obj.put("improveTomorrow", r.improveTomorrow ?: "")
            obj.put("mood", r.mood)
            obj.put("energyLevel", r.energyLevel)
            reflArr.put(obj)
        }
        root.put("reflections", reflArr)

        val entArr = org.json.JSONArray()
        entertainment.forEach { e ->
            val obj = org.json.JSONObject()
            obj.put("id", e.id)
            obj.put("category", e.category)
            obj.put("dateEpochDay", e.dateEpochDay)
            obj.put("durationMinutes", e.durationMinutes)
            obj.put("note", e.note ?: "")
            obj.put("createdAtEpochMilli", e.createdAtEpochMilli)
            if (e.isIntentional != null) {
                obj.put("isIntentional", e.isIntentional)
            }
            entArr.put(obj)
        }
        root.put("entertainmentLogs", entArr)

        val setObj = org.json.JSONObject()
        setObj.put("isOnboardingCompleted", settings.isOnboardingCompleted)
        setObj.put("isEveningReminderEnabled", settings.isEveningReminderEnabled)
        setObj.put("eveningReminderHour", settings.eveningReminderHour)
        setObj.put("eveningReminderMinute", settings.eveningReminderMinute)
        if (settings.weeklyPlayReferenceHours != null) {
            setObj.put("weeklyPlayReferenceHours", settings.weeklyPlayReferenceHours)
        }
        root.put("settings", setObj)

        root.toString(2)
    }

    suspend fun importBackupJson(jsonString: String) = withContext(Dispatchers.IO) {
        val root = JSONObject(jsonString)
        if (!root.has("version")) {
            throw IllegalArgumentException("Invalid backup file: missing version field")
        }

        // Parse Tasks
        val tasksList = mutableListOf<TaskEntity>()
        val tasksArr = root.optJSONArray("tasks")
        if (tasksArr != null) {
            for (i in 0 until tasksArr.length()) {
                val obj = tasksArr.getJSONObject(i)
                tasksList.add(
                    TaskEntity(
                        id = obj.optLong("id", 0L),
                        title = obj.getString("title"),
                        description = obj.optString("description").ifBlank { null },
                        dateEpochDay = obj.getLong("dateEpochDay"),
                        isBigThree = obj.optBoolean("isBigThree", false),
                        isCompleted = obj.optBoolean("isCompleted", false),
                        priority = obj.optInt("priority", 0),
                        createdAtEpochMilli = obj.optLong("createdAtEpochMilli", System.currentTimeMillis()),
                        isRecurring = obj.optBoolean("isRecurring", false),
                        recurrenceRule = obj.optString("recurrenceRule").ifBlank { null }
                    )
                )
            }
        }

        // Parse Habits
        val habitsList = mutableListOf<HabitEntity>()
        val habitsArr = root.optJSONArray("habits")
        if (habitsArr != null) {
            for (i in 0 until habitsArr.length()) {
                val obj = habitsArr.getJSONObject(i)
                habitsList.add(
                    HabitEntity(
                        id = obj.optLong("id", 0L),
                        name = obj.getString("name"),
                        description = obj.optString("description").ifBlank { null },
                        frequencyPerWeek = obj.optInt("frequencyPerWeek", 7),
                        createdAtEpochMilli = obj.optLong("createdAtEpochMilli", System.currentTimeMillis())
                    )
                )
            }
        }

        // Parse HabitLogs
        val logsList = mutableListOf<HabitLogEntity>()
        val logsArr = root.optJSONArray("habitLogs")
        if (logsArr != null) {
            for (i in 0 until logsArr.length()) {
                val obj = logsArr.getJSONObject(i)
                logsList.add(
                    HabitLogEntity(
                        id = obj.optLong("id", 0L),
                        habitId = obj.getLong("habitId"),
                        dateEpochDay = obj.getLong("dateEpochDay"),
                        status = obj.optString("status", "NOT_SCHEDULED")
                    )
                )
            }
        }

        // Parse Reflections
        val reflList = mutableListOf<ReflectionEntity>()
        val reflArr = root.optJSONArray("reflections")
        if (reflArr != null) {
            for (i in 0 until reflArr.length()) {
                val obj = reflArr.getJSONObject(i)
                reflList.add(
                    ReflectionEntity(
                        id = obj.optLong("id", 0L),
                        dateEpochDay = obj.getLong("dateEpochDay"),
                        wentWell = obj.optString("wentWell").ifBlank { null },
                        distracted = obj.optString("distracted").ifBlank { null },
                        improveTomorrow = obj.optString("improveTomorrow").ifBlank { null },
                        mood = obj.optString("mood", "NEUTRAL"),
                        energyLevel = obj.optInt("energyLevel", 3)
                    )
                )
            }
        }

        // Parse Entertainment Logs
        val entList = mutableListOf<EntertainmentLogEntity>()
        val entArr = root.optJSONArray("entertainmentLogs")
        if (entArr != null) {
            for (i in 0 until entArr.length()) {
                val obj = entArr.getJSONObject(i)
                val isIntentional = if (obj.has("isIntentional") && !obj.isNull("isIntentional")) {
                    obj.getBoolean("isIntentional")
                } else null
                entList.add(
                    EntertainmentLogEntity(
                        id = obj.optLong("id", 0L),
                        category = obj.optString("category", "OTHER"),
                        dateEpochDay = obj.getLong("dateEpochDay"),
                        durationMinutes = obj.optInt("durationMinutes", 0),
                        note = obj.optString("note").ifBlank { null },
                        createdAtEpochMilli = obj.optLong("createdAtEpochMilli", System.currentTimeMillis()),
                        isIntentional = isIntentional
                    )
                )
            }
        }

        // Parse Settings
        val settingsEntity = root.optJSONObject("settings")?.let { setObj ->
            val weeklyPlayRef = if (setObj.has("weeklyPlayReferenceHours") && !setObj.isNull("weeklyPlayReferenceHours")) {
                setObj.getInt("weeklyPlayReferenceHours")
            } else null
            AppSettingsEntity(
                id = 1,
                isOnboardingCompleted = setObj.optBoolean("isOnboardingCompleted", false),
                isEveningReminderEnabled = setObj.optBoolean("isEveningReminderEnabled", true),
                eveningReminderHour = setObj.optInt("eveningReminderHour", 21),
                eveningReminderMinute = setObj.optInt("eveningReminderMinute", 0),
                weeklyPlayReferenceHours = weeklyPlayRef
            )
        }

        // Atomically replace all data inside a database transaction
        database.withTransaction {
            val db = database.openHelper.writableDatabase
            db.execSQL("DELETE FROM habit_logs")
            db.execSQL("DELETE FROM habits")
            db.execSQL("DELETE FROM tasks")
            db.execSQL("DELETE FROM reflections")
            db.execSQL("DELETE FROM entertainment_logs")

            tasksList.forEach { taskDao.insertTask(it) }
            habitsList.forEach { habitDao.insertHabit(it) }
            logsList.forEach { habitLogDao.insertLog(it) }
            reflList.forEach { reflectionDao.insertReflection(it) }
            entList.forEach { entertainmentDao.insertLog(it) }
            if (settingsEntity != null) {
                appSettingsDao.insertOrUpdateSettings(settingsEntity)
            }
        }
    }
}
