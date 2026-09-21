package com.momentum.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Main Room database for Momentum.
 * Increment [version] and add a migration whenever you change the schema.
 */
@Database(
    entities = [
        TaskEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        ReflectionEntity::class,
        EntertainmentLogEntity::class,
        AppSettingsEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class MomentumDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
    abstract fun reflectionDao(): ReflectionDao
    abstract fun entertainmentDao(): EntertainmentDao
    abstract fun appSettingsDao(): AppSettingsDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE tasks ADD COLUMN isRecurring INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE tasks ADD COLUMN recurrenceRule TEXT DEFAULT NULL")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS entertainment_logs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        category TEXT NOT NULL,
                        dateEpochDay INTEGER NOT NULL,
                        durationMinutes INTEGER NOT NULL,
                        note TEXT,
                        createdAtEpochMilli INTEGER NOT NULL
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS index_entertainment_logs_dateEpochDay ON entertainment_logs(dateEpochDay)")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS app_settings (
                        id INTEGER PRIMARY KEY NOT NULL,
                        isOnboardingCompleted INTEGER NOT NULL DEFAULT 0,
                        isEveningReminderEnabled INTEGER NOT NULL DEFAULT 1,
                        eveningReminderHour INTEGER NOT NULL DEFAULT 21,
                        eveningReminderMinute INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE entertainment_logs ADD COLUMN isIntentional INTEGER DEFAULT NULL")
                db.execSQL("ALTER TABLE app_settings ADD COLUMN weeklyPlayReferenceHours INTEGER DEFAULT NULL")
            }
        }
    }
}
