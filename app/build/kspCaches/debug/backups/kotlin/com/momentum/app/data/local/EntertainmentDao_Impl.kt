package com.momentum.app.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
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
public class EntertainmentDao_Impl(
  __db: RoomDatabase,
) : EntertainmentDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfEntertainmentLogEntity: EntityInsertAdapter<EntertainmentLogEntity>

  private val __deleteAdapterOfEntertainmentLogEntity:
      EntityDeleteOrUpdateAdapter<EntertainmentLogEntity>

  private val __updateAdapterOfEntertainmentLogEntity:
      EntityDeleteOrUpdateAdapter<EntertainmentLogEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfEntertainmentLogEntity = object :
        EntityInsertAdapter<EntertainmentLogEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `entertainment_logs` (`id`,`category`,`dateEpochDay`,`durationMinutes`,`note`,`createdAtEpochMilli`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: EntertainmentLogEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.category)
        statement.bindLong(3, entity.dateEpochDay)
        statement.bindLong(4, entity.durationMinutes.toLong())
        val _tmpNote: String? = entity.note
        if (_tmpNote == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpNote)
        }
        statement.bindLong(6, entity.createdAtEpochMilli)
      }
    }
    this.__deleteAdapterOfEntertainmentLogEntity = object :
        EntityDeleteOrUpdateAdapter<EntertainmentLogEntity>() {
      protected override fun createQuery(): String =
          "DELETE FROM `entertainment_logs` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: EntertainmentLogEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfEntertainmentLogEntity = object :
        EntityDeleteOrUpdateAdapter<EntertainmentLogEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `entertainment_logs` SET `id` = ?,`category` = ?,`dateEpochDay` = ?,`durationMinutes` = ?,`note` = ?,`createdAtEpochMilli` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: EntertainmentLogEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.category)
        statement.bindLong(3, entity.dateEpochDay)
        statement.bindLong(4, entity.durationMinutes.toLong())
        val _tmpNote: String? = entity.note
        if (_tmpNote == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpNote)
        }
        statement.bindLong(6, entity.createdAtEpochMilli)
        statement.bindLong(7, entity.id)
      }
    }
  }

  public override suspend fun insertLog(log: EntertainmentLogEntity): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfEntertainmentLogEntity.insertAndReturnId(_connection, log)
    _result
  }

  public override suspend fun deleteLog(log: EntertainmentLogEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfEntertainmentLogEntity.handle(_connection, log)
  }

  public override suspend fun updateLog(log: EntertainmentLogEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfEntertainmentLogEntity.handle(_connection, log)
  }

  public override fun getAllLogs(): Flow<List<EntertainmentLogEntity>> {
    val _sql: String =
        "SELECT * FROM entertainment_logs ORDER BY dateEpochDay DESC, createdAtEpochMilli DESC"
    return createFlow(__db, false, arrayOf("entertainment_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfDurationMinutes: Int = getColumnIndexOrThrow(_stmt, "durationMinutes")
        val _columnIndexOfNote: Int = getColumnIndexOrThrow(_stmt, "note")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _result: MutableList<EntertainmentLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EntertainmentLogEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpDurationMinutes: Int
          _tmpDurationMinutes = _stmt.getLong(_columnIndexOfDurationMinutes).toInt()
          val _tmpNote: String?
          if (_stmt.isNull(_columnIndexOfNote)) {
            _tmpNote = null
          } else {
            _tmpNote = _stmt.getText(_columnIndexOfNote)
          }
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          _item =
              EntertainmentLogEntity(_tmpId,_tmpCategory,_tmpDateEpochDay,_tmpDurationMinutes,_tmpNote,_tmpCreatedAtEpochMilli)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLogsForDate(epochDay: Long): Flow<List<EntertainmentLogEntity>> {
    val _sql: String =
        "SELECT * FROM entertainment_logs WHERE dateEpochDay = ? ORDER BY createdAtEpochMilli ASC"
    return createFlow(__db, false, arrayOf("entertainment_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, epochDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfDurationMinutes: Int = getColumnIndexOrThrow(_stmt, "durationMinutes")
        val _columnIndexOfNote: Int = getColumnIndexOrThrow(_stmt, "note")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _result: MutableList<EntertainmentLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EntertainmentLogEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpDurationMinutes: Int
          _tmpDurationMinutes = _stmt.getLong(_columnIndexOfDurationMinutes).toInt()
          val _tmpNote: String?
          if (_stmt.isNull(_columnIndexOfNote)) {
            _tmpNote = null
          } else {
            _tmpNote = _stmt.getText(_columnIndexOfNote)
          }
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          _item =
              EntertainmentLogEntity(_tmpId,_tmpCategory,_tmpDateEpochDay,_tmpDurationMinutes,_tmpNote,_tmpCreatedAtEpochMilli)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLogsForWeek(startDay: Long, endDay: Long):
      Flow<List<EntertainmentLogEntity>> {
    val _sql: String =
        "SELECT * FROM entertainment_logs WHERE dateEpochDay >= ? AND dateEpochDay <= ? ORDER BY dateEpochDay ASC"
    return createFlow(__db, false, arrayOf("entertainment_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startDay)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfDurationMinutes: Int = getColumnIndexOrThrow(_stmt, "durationMinutes")
        val _columnIndexOfNote: Int = getColumnIndexOrThrow(_stmt, "note")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _result: MutableList<EntertainmentLogEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: EntertainmentLogEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpDurationMinutes: Int
          _tmpDurationMinutes = _stmt.getLong(_columnIndexOfDurationMinutes).toInt()
          val _tmpNote: String?
          if (_stmt.isNull(_columnIndexOfNote)) {
            _tmpNote = null
          } else {
            _tmpNote = _stmt.getText(_columnIndexOfNote)
          }
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          _item =
              EntertainmentLogEntity(_tmpId,_tmpCategory,_tmpDateEpochDay,_tmpDurationMinutes,_tmpNote,_tmpCreatedAtEpochMilli)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteLogById(id: Long) {
    val _sql: String = "DELETE FROM entertainment_logs WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
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
