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
public class HabitDao_Impl(
  __db: RoomDatabase,
) : HabitDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfHabitEntity: EntityInsertAdapter<HabitEntity>

  private val __deleteAdapterOfHabitEntity: EntityDeleteOrUpdateAdapter<HabitEntity>

  private val __updateAdapterOfHabitEntity: EntityDeleteOrUpdateAdapter<HabitEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfHabitEntity = object : EntityInsertAdapter<HabitEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `habits` (`id`,`name`,`description`,`frequencyPerWeek`,`createdAtEpochMilli`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: HabitEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        statement.bindLong(4, entity.frequencyPerWeek.toLong())
        statement.bindLong(5, entity.createdAtEpochMilli)
      }
    }
    this.__deleteAdapterOfHabitEntity = object : EntityDeleteOrUpdateAdapter<HabitEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `habits` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: HabitEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfHabitEntity = object : EntityDeleteOrUpdateAdapter<HabitEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `habits` SET `id` = ?,`name` = ?,`description` = ?,`frequencyPerWeek` = ?,`createdAtEpochMilli` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: HabitEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        statement.bindLong(4, entity.frequencyPerWeek.toLong())
        statement.bindLong(5, entity.createdAtEpochMilli)
        statement.bindLong(6, entity.id)
      }
    }
  }

  public override suspend fun insertHabit(habit: HabitEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfHabitEntity.insertAndReturnId(_connection, habit)
    _result
  }

  public override suspend fun deleteHabit(habit: HabitEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfHabitEntity.handle(_connection, habit)
  }

  public override suspend fun updateHabit(habit: HabitEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfHabitEntity.handle(_connection, habit)
  }

  public override fun getAllHabits(): Flow<List<HabitEntity>> {
    val _sql: String = "SELECT * FROM habits ORDER BY createdAtEpochMilli ASC"
    return createFlow(__db, false, arrayOf("habits")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfFrequencyPerWeek: Int = getColumnIndexOrThrow(_stmt, "frequencyPerWeek")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _result: MutableList<HabitEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: HabitEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpFrequencyPerWeek: Int
          _tmpFrequencyPerWeek = _stmt.getLong(_columnIndexOfFrequencyPerWeek).toInt()
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          _item =
              HabitEntity(_tmpId,_tmpName,_tmpDescription,_tmpFrequencyPerWeek,_tmpCreatedAtEpochMilli)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getHabitById(id: Long): HabitEntity? {
    val _sql: String = "SELECT * FROM habits WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfFrequencyPerWeek: Int = getColumnIndexOrThrow(_stmt, "frequencyPerWeek")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _result: HabitEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpFrequencyPerWeek: Int
          _tmpFrequencyPerWeek = _stmt.getLong(_columnIndexOfFrequencyPerWeek).toInt()
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          _result =
              HabitEntity(_tmpId,_tmpName,_tmpDescription,_tmpFrequencyPerWeek,_tmpCreatedAtEpochMilli)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteHabitById(id: Long) {
    val _sql: String = "DELETE FROM habits WHERE id = ?"
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
