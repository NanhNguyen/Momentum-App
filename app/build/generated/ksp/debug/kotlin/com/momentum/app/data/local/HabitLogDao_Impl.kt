package com.momentum.app.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performInTransactionSuspending
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class HabitLogDao_Impl(
  __db: RoomDatabase,
) : HabitLogDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfHabitLogEntity: EntityInsertAdapter<HabitLogEntity>

  private val __updateAdapterOfHabitLogEntity: EntityDeleteOrUpdateAdapter<HabitLogEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfHabitLogEntity = object : EntityInsertAdapter<HabitLogEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `habit_logs` (`id`,`habitId`,`dateEpochDay`,`status`) VALUES (nullif(?, 0),?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: HabitLogEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.habitId)
        statement.bindLong(3, entity.dateEpochDay)
        statement.bindText(4, entity.status)
      }
    }
    this.__updateAdapterOfHabitLogEntity = object : EntityDeleteOrUpdateAdapter<HabitLogEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `habit_logs` SET `id` = ?,`habitId` = ?,`dateEpochDay` = ?,`status` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: HabitLogEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.habitId)
        statement.bindLong(3, entity.dateEpochDay)
        statement.bindText(4, entity.status)
        statement.bindLong(5, entity.id)
      }
    }
  }

  public override suspend fun insertLog(log: HabitLogEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfHabitLogEntity.insertAndReturnId(_connection, log)
    _result
  }

  public override suspend fun updateLog(log: HabitLogEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfHabitLogEntity.handle(_connection, log)
  }

  public override suspend fun upsertLog(log: HabitLogEntity): Unit =
      performInTransactionSuspending(__db) {
    super@HabitLogDao_Impl.upsertLog(log)
  }

  public override fun getAllLogs(): Flow<List<HabitLogEntity>> {
    val _sql: String = "SELECT * FROM habit_logs"
    return createFlow(__db, false, arrayOf("habit_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfHabitId: Int = getColumnIndexOrThrow(_stmt, "habitId")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _result: MutableList<HabitLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HabitLogEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpHabitId: Long
          _tmpHabitId = _stmt.getLong(_columnIndexOfHabitId)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          _item = HabitLogEntity(_tmpId,_tmpHabitId,_tmpDateEpochDay,_tmpStatus)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLogsForHabit(habitId: Long): Flow<List<HabitLogEntity>> {
    val _sql: String = "SELECT * FROM habit_logs WHERE habitId = ? ORDER BY dateEpochDay ASC"
    return createFlow(__db, false, arrayOf("habit_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, habitId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfHabitId: Int = getColumnIndexOrThrow(_stmt, "habitId")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _result: MutableList<HabitLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HabitLogEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpHabitId: Long
          _tmpHabitId = _stmt.getLong(_columnIndexOfHabitId)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          _item = HabitLogEntity(_tmpId,_tmpHabitId,_tmpDateEpochDay,_tmpStatus)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLogsForHabitInRange(
    habitId: Long,
    startDay: Long,
    endDay: Long,
  ): Flow<List<HabitLogEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM habit_logs 
        |        WHERE habitId = ? AND dateEpochDay >= ? AND dateEpochDay <= ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("habit_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, habitId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, startDay)
        _argIndex = 3
        _stmt.bindLong(_argIndex, endDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfHabitId: Int = getColumnIndexOrThrow(_stmt, "habitId")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _result: MutableList<HabitLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HabitLogEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpHabitId: Long
          _tmpHabitId = _stmt.getLong(_columnIndexOfHabitId)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          _item = HabitLogEntity(_tmpId,_tmpHabitId,_tmpDateEpochDay,_tmpStatus)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllLogsForWeek(startDay: Long, endDay: Long): Flow<List<HabitLogEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM habit_logs 
        |        WHERE dateEpochDay >= ? AND dateEpochDay <= ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("habit_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startDay)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfHabitId: Int = getColumnIndexOrThrow(_stmt, "habitId")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _result: MutableList<HabitLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HabitLogEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpHabitId: Long
          _tmpHabitId = _stmt.getLong(_columnIndexOfHabitId)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          _item = HabitLogEntity(_tmpId,_tmpHabitId,_tmpDateEpochDay,_tmpStatus)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getLogForHabitOnDate(habitId: Long, epochDay: Long): HabitLogEntity? {
    val _sql: String = "SELECT * FROM habit_logs WHERE habitId = ? AND dateEpochDay = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, habitId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, epochDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfHabitId: Int = getColumnIndexOrThrow(_stmt, "habitId")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _result: HabitLogEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpHabitId: Long
          _tmpHabitId = _stmt.getLong(_columnIndexOfHabitId)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          _result = HabitLogEntity(_tmpId,_tmpHabitId,_tmpDateEpochDay,_tmpStatus)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteLogsForHabit(habitId: Long) {
    val _sql: String = "DELETE FROM habit_logs WHERE habitId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, habitId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
