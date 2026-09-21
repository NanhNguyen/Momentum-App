package com.momentum.app.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppSettingsDao_Impl(
  __db: RoomDatabase,
) : AppSettingsDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAppSettingsEntity: EntityInsertAdapter<AppSettingsEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfAppSettingsEntity = object : EntityInsertAdapter<AppSettingsEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `app_settings` (`id`,`isOnboardingCompleted`,`isEveningReminderEnabled`,`eveningReminderHour`,`eveningReminderMinute`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AppSettingsEntity) {
        statement.bindLong(1, entity.id.toLong())
        val _tmp: Int = if (entity.isOnboardingCompleted) 1 else 0
        statement.bindLong(2, _tmp.toLong())
        val _tmp_1: Int = if (entity.isEveningReminderEnabled) 1 else 0
        statement.bindLong(3, _tmp_1.toLong())
        statement.bindLong(4, entity.eveningReminderHour.toLong())
        statement.bindLong(5, entity.eveningReminderMinute.toLong())
      }
    }
  }

  public override suspend fun insertOrUpdateSettings(settings: AppSettingsEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfAppSettingsEntity.insert(_connection, settings)
  }

  public override fun getSettings(): Flow<AppSettingsEntity?> {
    val _sql: String = "SELECT * FROM app_settings WHERE id = 1 LIMIT 1"
    return createFlow(__db, false, arrayOf("app_settings")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfIsOnboardingCompleted: Int = getColumnIndexOrThrow(_stmt,
            "isOnboardingCompleted")
        val _columnIndexOfIsEveningReminderEnabled: Int = getColumnIndexOrThrow(_stmt,
            "isEveningReminderEnabled")
        val _columnIndexOfEveningReminderHour: Int = getColumnIndexOrThrow(_stmt,
            "eveningReminderHour")
        val _columnIndexOfEveningReminderMinute: Int = getColumnIndexOrThrow(_stmt,
            "eveningReminderMinute")
        val _result: AppSettingsEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpIsOnboardingCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsOnboardingCompleted).toInt()
          _tmpIsOnboardingCompleted = _tmp != 0
          val _tmpIsEveningReminderEnabled: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsEveningReminderEnabled).toInt()
          _tmpIsEveningReminderEnabled = _tmp_1 != 0
          val _tmpEveningReminderHour: Int
          _tmpEveningReminderHour = _stmt.getLong(_columnIndexOfEveningReminderHour).toInt()
          val _tmpEveningReminderMinute: Int
          _tmpEveningReminderMinute = _stmt.getLong(_columnIndexOfEveningReminderMinute).toInt()
          _result =
              AppSettingsEntity(_tmpId,_tmpIsOnboardingCompleted,_tmpIsEveningReminderEnabled,_tmpEveningReminderHour,_tmpEveningReminderMinute)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getSettingsSync(): AppSettingsEntity? {
    val _sql: String = "SELECT * FROM app_settings WHERE id = 1 LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfIsOnboardingCompleted: Int = getColumnIndexOrThrow(_stmt,
            "isOnboardingCompleted")
        val _columnIndexOfIsEveningReminderEnabled: Int = getColumnIndexOrThrow(_stmt,
            "isEveningReminderEnabled")
        val _columnIndexOfEveningReminderHour: Int = getColumnIndexOrThrow(_stmt,
            "eveningReminderHour")
        val _columnIndexOfEveningReminderMinute: Int = getColumnIndexOrThrow(_stmt,
            "eveningReminderMinute")
        val _result: AppSettingsEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpIsOnboardingCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsOnboardingCompleted).toInt()
          _tmpIsOnboardingCompleted = _tmp != 0
          val _tmpIsEveningReminderEnabled: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsEveningReminderEnabled).toInt()
          _tmpIsEveningReminderEnabled = _tmp_1 != 0
          val _tmpEveningReminderHour: Int
          _tmpEveningReminderHour = _stmt.getLong(_columnIndexOfEveningReminderHour).toInt()
          val _tmpEveningReminderMinute: Int
          _tmpEveningReminderMinute = _stmt.getLong(_columnIndexOfEveningReminderMinute).toInt()
          _result =
              AppSettingsEntity(_tmpId,_tmpIsOnboardingCompleted,_tmpIsEveningReminderEnabled,_tmpEveningReminderHour,_tmpEveningReminderMinute)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
