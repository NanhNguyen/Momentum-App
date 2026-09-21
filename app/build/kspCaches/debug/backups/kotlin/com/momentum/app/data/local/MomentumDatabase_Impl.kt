package com.momentum.app.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MomentumDatabase_Impl : MomentumDatabase() {
  private val _taskDao: Lazy<TaskDao> = lazy {
    TaskDao_Impl(this)
  }

  private val _habitDao: Lazy<HabitDao> = lazy {
    HabitDao_Impl(this)
  }

  private val _habitLogDao: Lazy<HabitLogDao> = lazy {
    HabitLogDao_Impl(this)
  }

  private val _reflectionDao: Lazy<ReflectionDao> = lazy {
    ReflectionDao_Impl(this)
  }

  private val _entertainmentDao: Lazy<EntertainmentDao> = lazy {
    EntertainmentDao_Impl(this)
  }

  private val _appSettingsDao: Lazy<AppSettingsDao> = lazy {
    AppSettingsDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(3,
        "1307f661f26a6d045cfa62111eae719e", "505fc910619e126ce520a41f33a03bb1") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `dateEpochDay` INTEGER NOT NULL, `isBigThree` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `priority` INTEGER NOT NULL, `createdAtEpochMilli` INTEGER NOT NULL, `isRecurring` INTEGER NOT NULL, `recurrenceRule` TEXT)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_dateEpochDay` ON `tasks` (`dateEpochDay`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `habits` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `frequencyPerWeek` INTEGER NOT NULL, `createdAtEpochMilli` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `habit_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `habitId` INTEGER NOT NULL, `dateEpochDay` INTEGER NOT NULL, `status` TEXT NOT NULL, FOREIGN KEY(`habitId`) REFERENCES `habits`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_habit_logs_habitId_dateEpochDay` ON `habit_logs` (`habitId`, `dateEpochDay`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `reflections` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dateEpochDay` INTEGER NOT NULL, `wentWell` TEXT, `distracted` TEXT, `improveTomorrow` TEXT, `mood` TEXT NOT NULL, `energyLevel` INTEGER NOT NULL)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_reflections_dateEpochDay` ON `reflections` (`dateEpochDay`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `entertainment_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `category` TEXT NOT NULL, `dateEpochDay` INTEGER NOT NULL, `durationMinutes` INTEGER NOT NULL, `note` TEXT, `createdAtEpochMilli` INTEGER NOT NULL)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_entertainment_logs_dateEpochDay` ON `entertainment_logs` (`dateEpochDay`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `app_settings` (`id` INTEGER NOT NULL, `isOnboardingCompleted` INTEGER NOT NULL, `isEveningReminderEnabled` INTEGER NOT NULL, `eveningReminderHour` INTEGER NOT NULL, `eveningReminderMinute` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1307f661f26a6d045cfa62111eae719e')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `tasks`")
        connection.execSQL("DROP TABLE IF EXISTS `habits`")
        connection.execSQL("DROP TABLE IF EXISTS `habit_logs`")
        connection.execSQL("DROP TABLE IF EXISTS `reflections`")
        connection.execSQL("DROP TABLE IF EXISTS `entertainment_logs`")
        connection.execSQL("DROP TABLE IF EXISTS `app_settings`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsTasks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTasks.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("description", TableInfo.Column("description", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("dateEpochDay", TableInfo.Column("dateEpochDay", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("isBigThree", TableInfo.Column("isBigThree", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("priority", TableInfo.Column("priority", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("createdAtEpochMilli", TableInfo.Column("createdAtEpochMilli", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("isRecurring", TableInfo.Column("isRecurring", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTasks.put("recurrenceRule", TableInfo.Column("recurrenceRule", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTasks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTasks: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesTasks.add(TableInfo.Index("index_tasks_dateEpochDay", false, listOf("dateEpochDay"),
            listOf("ASC")))
        val _infoTasks: TableInfo = TableInfo("tasks", _columnsTasks, _foreignKeysTasks,
            _indicesTasks)
        val _existingTasks: TableInfo = read(connection, "tasks")
        if (!_infoTasks.equals(_existingTasks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |tasks(com.momentum.app.data.local.TaskEntity).
              | Expected:
              |""".trimMargin() + _infoTasks + """
              |
              | Found:
              |""".trimMargin() + _existingTasks)
        }
        val _columnsHabits: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsHabits.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("description", TableInfo.Column("description", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("frequencyPerWeek", TableInfo.Column("frequencyPerWeek", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHabits.put("createdAtEpochMilli", TableInfo.Column("createdAtEpochMilli", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysHabits: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesHabits: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoHabits: TableInfo = TableInfo("habits", _columnsHabits, _foreignKeysHabits,
            _indicesHabits)
        val _existingHabits: TableInfo = read(connection, "habits")
        if (!_infoHabits.equals(_existingHabits)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |habits(com.momentum.app.data.local.HabitEntity).
              | Expected:
              |""".trimMargin() + _infoHabits + """
              |
              | Found:
              |""".trimMargin() + _existingHabits)
        }
        val _columnsHabitLogs: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsHabitLogs.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("habitId", TableInfo.Column("habitId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("dateEpochDay", TableInfo.Column("dateEpochDay", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsHabitLogs.put("status", TableInfo.Column("status", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysHabitLogs: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysHabitLogs.add(TableInfo.ForeignKey("habits", "CASCADE", "NO ACTION",
            listOf("habitId"), listOf("id")))
        val _indicesHabitLogs: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesHabitLogs.add(TableInfo.Index("index_habit_logs_habitId_dateEpochDay", true,
            listOf("habitId", "dateEpochDay"), listOf("ASC", "ASC")))
        val _infoHabitLogs: TableInfo = TableInfo("habit_logs", _columnsHabitLogs,
            _foreignKeysHabitLogs, _indicesHabitLogs)
        val _existingHabitLogs: TableInfo = read(connection, "habit_logs")
        if (!_infoHabitLogs.equals(_existingHabitLogs)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |habit_logs(com.momentum.app.data.local.HabitLogEntity).
              | Expected:
              |""".trimMargin() + _infoHabitLogs + """
              |
              | Found:
              |""".trimMargin() + _existingHabitLogs)
        }
        val _columnsReflections: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsReflections.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReflections.put("dateEpochDay", TableInfo.Column("dateEpochDay", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReflections.put("wentWell", TableInfo.Column("wentWell", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReflections.put("distracted", TableInfo.Column("distracted", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReflections.put("improveTomorrow", TableInfo.Column("improveTomorrow", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReflections.put("mood", TableInfo.Column("mood", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReflections.put("energyLevel", TableInfo.Column("energyLevel", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysReflections: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesReflections: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesReflections.add(TableInfo.Index("index_reflections_dateEpochDay", true,
            listOf("dateEpochDay"), listOf("ASC")))
        val _infoReflections: TableInfo = TableInfo("reflections", _columnsReflections,
            _foreignKeysReflections, _indicesReflections)
        val _existingReflections: TableInfo = read(connection, "reflections")
        if (!_infoReflections.equals(_existingReflections)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |reflections(com.momentum.app.data.local.ReflectionEntity).
              | Expected:
              |""".trimMargin() + _infoReflections + """
              |
              | Found:
              |""".trimMargin() + _existingReflections)
        }
        val _columnsEntertainmentLogs: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsEntertainmentLogs.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEntertainmentLogs.put("category", TableInfo.Column("category", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEntertainmentLogs.put("dateEpochDay", TableInfo.Column("dateEpochDay", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEntertainmentLogs.put("durationMinutes", TableInfo.Column("durationMinutes",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEntertainmentLogs.put("note", TableInfo.Column("note", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsEntertainmentLogs.put("createdAtEpochMilli", TableInfo.Column("createdAtEpochMilli",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysEntertainmentLogs: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesEntertainmentLogs: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesEntertainmentLogs.add(TableInfo.Index("index_entertainment_logs_dateEpochDay",
            false, listOf("dateEpochDay"), listOf("ASC")))
        val _infoEntertainmentLogs: TableInfo = TableInfo("entertainment_logs",
            _columnsEntertainmentLogs, _foreignKeysEntertainmentLogs, _indicesEntertainmentLogs)
        val _existingEntertainmentLogs: TableInfo = read(connection, "entertainment_logs")
        if (!_infoEntertainmentLogs.equals(_existingEntertainmentLogs)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |entertainment_logs(com.momentum.app.data.local.EntertainmentLogEntity).
              | Expected:
              |""".trimMargin() + _infoEntertainmentLogs + """
              |
              | Found:
              |""".trimMargin() + _existingEntertainmentLogs)
        }
        val _columnsAppSettings: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAppSettings.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSettings.put("isOnboardingCompleted", TableInfo.Column("isOnboardingCompleted",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSettings.put("isEveningReminderEnabled",
            TableInfo.Column("isEveningReminderEnabled", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSettings.put("eveningReminderHour", TableInfo.Column("eveningReminderHour",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppSettings.put("eveningReminderMinute", TableInfo.Column("eveningReminderMinute",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAppSettings: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAppSettings: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoAppSettings: TableInfo = TableInfo("app_settings", _columnsAppSettings,
            _foreignKeysAppSettings, _indicesAppSettings)
        val _existingAppSettings: TableInfo = read(connection, "app_settings")
        if (!_infoAppSettings.equals(_existingAppSettings)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |app_settings(com.momentum.app.data.local.AppSettingsEntity).
              | Expected:
              |""".trimMargin() + _infoAppSettings + """
              |
              | Found:
              |""".trimMargin() + _existingAppSettings)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "tasks", "habits", "habit_logs",
        "reflections", "entertainment_logs", "app_settings")
  }

  public override fun clearAllTables() {
    super.performClear(true, "tasks", "habits", "habit_logs", "reflections", "entertainment_logs",
        "app_settings")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(TaskDao::class, TaskDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(HabitDao::class, HabitDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(HabitLogDao::class, HabitLogDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ReflectionDao::class, ReflectionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(EntertainmentDao::class, EntertainmentDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(AppSettingsDao::class, AppSettingsDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun taskDao(): TaskDao = _taskDao.value

  public override fun habitDao(): HabitDao = _habitDao.value

  public override fun habitLogDao(): HabitLogDao = _habitLogDao.value

  public override fun reflectionDao(): ReflectionDao = _reflectionDao.value

  public override fun entertainmentDao(): EntertainmentDao = _entertainmentDao.value

  public override fun appSettingsDao(): AppSettingsDao = _appSettingsDao.value
}
