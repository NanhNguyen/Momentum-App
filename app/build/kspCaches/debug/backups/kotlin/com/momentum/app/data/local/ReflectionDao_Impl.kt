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
public class ReflectionDao_Impl(
  __db: RoomDatabase,
) : ReflectionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfReflectionEntity: EntityInsertAdapter<ReflectionEntity>

  private val __deleteAdapterOfReflectionEntity: EntityDeleteOrUpdateAdapter<ReflectionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfReflectionEntity = object : EntityInsertAdapter<ReflectionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `reflections` (`id`,`dateEpochDay`,`wentWell`,`distracted`,`improveTomorrow`,`mood`,`energyLevel`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ReflectionEntity) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.dateEpochDay)
        val _tmpWentWell: String? = entity.wentWell
        if (_tmpWentWell == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpWentWell)
        }
        val _tmpDistracted: String? = entity.distracted
        if (_tmpDistracted == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpDistracted)
        }
        val _tmpImproveTomorrow: String? = entity.improveTomorrow
        if (_tmpImproveTomorrow == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpImproveTomorrow)
        }
        statement.bindText(6, entity.mood)
        statement.bindLong(7, entity.energyLevel.toLong())
      }
    }
    this.__deleteAdapterOfReflectionEntity = object :
        EntityDeleteOrUpdateAdapter<ReflectionEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `reflections` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ReflectionEntity) {
        statement.bindLong(1, entity.id)
      }
    }
  }

  public override suspend fun insertReflection(entry: ReflectionEntity): Long =
      performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfReflectionEntity.insertAndReturnId(_connection, entry)
    _result
  }

  public override suspend fun deleteReflection(entry: ReflectionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfReflectionEntity.handle(_connection, entry)
  }

  public override suspend fun upsertReflection(entry: ReflectionEntity): Unit =
      performInTransactionSuspending(__db) {
    super@ReflectionDao_Impl.upsertReflection(entry)
  }

  public override fun getAllReflections(): Flow<List<ReflectionEntity>> {
    val _sql: String = "SELECT * FROM reflections ORDER BY dateEpochDay DESC"
    return createFlow(__db, false, arrayOf("reflections")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfWentWell: Int = getColumnIndexOrThrow(_stmt, "wentWell")
        val _columnIndexOfDistracted: Int = getColumnIndexOrThrow(_stmt, "distracted")
        val _columnIndexOfImproveTomorrow: Int = getColumnIndexOrThrow(_stmt, "improveTomorrow")
        val _columnIndexOfMood: Int = getColumnIndexOrThrow(_stmt, "mood")
        val _columnIndexOfEnergyLevel: Int = getColumnIndexOrThrow(_stmt, "energyLevel")
        val _result: MutableList<ReflectionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReflectionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpWentWell: String?
          if (_stmt.isNull(_columnIndexOfWentWell)) {
            _tmpWentWell = null
          } else {
            _tmpWentWell = _stmt.getText(_columnIndexOfWentWell)
          }
          val _tmpDistracted: String?
          if (_stmt.isNull(_columnIndexOfDistracted)) {
            _tmpDistracted = null
          } else {
            _tmpDistracted = _stmt.getText(_columnIndexOfDistracted)
          }
          val _tmpImproveTomorrow: String?
          if (_stmt.isNull(_columnIndexOfImproveTomorrow)) {
            _tmpImproveTomorrow = null
          } else {
            _tmpImproveTomorrow = _stmt.getText(_columnIndexOfImproveTomorrow)
          }
          val _tmpMood: String
          _tmpMood = _stmt.getText(_columnIndexOfMood)
          val _tmpEnergyLevel: Int
          _tmpEnergyLevel = _stmt.getLong(_columnIndexOfEnergyLevel).toInt()
          _item =
              ReflectionEntity(_tmpId,_tmpDateEpochDay,_tmpWentWell,_tmpDistracted,_tmpImproveTomorrow,_tmpMood,_tmpEnergyLevel)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getReflectionForDate(epochDay: Long): Flow<ReflectionEntity?> {
    val _sql: String = "SELECT * FROM reflections WHERE dateEpochDay = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("reflections")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, epochDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfWentWell: Int = getColumnIndexOrThrow(_stmt, "wentWell")
        val _columnIndexOfDistracted: Int = getColumnIndexOrThrow(_stmt, "distracted")
        val _columnIndexOfImproveTomorrow: Int = getColumnIndexOrThrow(_stmt, "improveTomorrow")
        val _columnIndexOfMood: Int = getColumnIndexOrThrow(_stmt, "mood")
        val _columnIndexOfEnergyLevel: Int = getColumnIndexOrThrow(_stmt, "energyLevel")
        val _result: ReflectionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpWentWell: String?
          if (_stmt.isNull(_columnIndexOfWentWell)) {
            _tmpWentWell = null
          } else {
            _tmpWentWell = _stmt.getText(_columnIndexOfWentWell)
          }
          val _tmpDistracted: String?
          if (_stmt.isNull(_columnIndexOfDistracted)) {
            _tmpDistracted = null
          } else {
            _tmpDistracted = _stmt.getText(_columnIndexOfDistracted)
          }
          val _tmpImproveTomorrow: String?
          if (_stmt.isNull(_columnIndexOfImproveTomorrow)) {
            _tmpImproveTomorrow = null
          } else {
            _tmpImproveTomorrow = _stmt.getText(_columnIndexOfImproveTomorrow)
          }
          val _tmpMood: String
          _tmpMood = _stmt.getText(_columnIndexOfMood)
          val _tmpEnergyLevel: Int
          _tmpEnergyLevel = _stmt.getLong(_columnIndexOfEnergyLevel).toInt()
          _result =
              ReflectionEntity(_tmpId,_tmpDateEpochDay,_tmpWentWell,_tmpDistracted,_tmpImproveTomorrow,_tmpMood,_tmpEnergyLevel)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getReflectionForDateSync(epochDay: Long): ReflectionEntity? {
    val _sql: String = "SELECT * FROM reflections WHERE dateEpochDay = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, epochDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfWentWell: Int = getColumnIndexOrThrow(_stmt, "wentWell")
        val _columnIndexOfDistracted: Int = getColumnIndexOrThrow(_stmt, "distracted")
        val _columnIndexOfImproveTomorrow: Int = getColumnIndexOrThrow(_stmt, "improveTomorrow")
        val _columnIndexOfMood: Int = getColumnIndexOrThrow(_stmt, "mood")
        val _columnIndexOfEnergyLevel: Int = getColumnIndexOrThrow(_stmt, "energyLevel")
        val _result: ReflectionEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpWentWell: String?
          if (_stmt.isNull(_columnIndexOfWentWell)) {
            _tmpWentWell = null
          } else {
            _tmpWentWell = _stmt.getText(_columnIndexOfWentWell)
          }
          val _tmpDistracted: String?
          if (_stmt.isNull(_columnIndexOfDistracted)) {
            _tmpDistracted = null
          } else {
            _tmpDistracted = _stmt.getText(_columnIndexOfDistracted)
          }
          val _tmpImproveTomorrow: String?
          if (_stmt.isNull(_columnIndexOfImproveTomorrow)) {
            _tmpImproveTomorrow = null
          } else {
            _tmpImproveTomorrow = _stmt.getText(_columnIndexOfImproveTomorrow)
          }
          val _tmpMood: String
          _tmpMood = _stmt.getText(_columnIndexOfMood)
          val _tmpEnergyLevel: Int
          _tmpEnergyLevel = _stmt.getLong(_columnIndexOfEnergyLevel).toInt()
          _result =
              ReflectionEntity(_tmpId,_tmpDateEpochDay,_tmpWentWell,_tmpDistracted,_tmpImproveTomorrow,_tmpMood,_tmpEnergyLevel)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getReflectionsForWeek(startDay: Long, endDay: Long):
      Flow<List<ReflectionEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM reflections 
        |        WHERE dateEpochDay >= ? AND dateEpochDay <= ? 
        |        ORDER BY dateEpochDay ASC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("reflections")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startDay)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfWentWell: Int = getColumnIndexOrThrow(_stmt, "wentWell")
        val _columnIndexOfDistracted: Int = getColumnIndexOrThrow(_stmt, "distracted")
        val _columnIndexOfImproveTomorrow: Int = getColumnIndexOrThrow(_stmt, "improveTomorrow")
        val _columnIndexOfMood: Int = getColumnIndexOrThrow(_stmt, "mood")
        val _columnIndexOfEnergyLevel: Int = getColumnIndexOrThrow(_stmt, "energyLevel")
        val _result: MutableList<ReflectionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReflectionEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpWentWell: String?
          if (_stmt.isNull(_columnIndexOfWentWell)) {
            _tmpWentWell = null
          } else {
            _tmpWentWell = _stmt.getText(_columnIndexOfWentWell)
          }
          val _tmpDistracted: String?
          if (_stmt.isNull(_columnIndexOfDistracted)) {
            _tmpDistracted = null
          } else {
            _tmpDistracted = _stmt.getText(_columnIndexOfDistracted)
          }
          val _tmpImproveTomorrow: String?
          if (_stmt.isNull(_columnIndexOfImproveTomorrow)) {
            _tmpImproveTomorrow = null
          } else {
            _tmpImproveTomorrow = _stmt.getText(_columnIndexOfImproveTomorrow)
          }
          val _tmpMood: String
          _tmpMood = _stmt.getText(_columnIndexOfMood)
          val _tmpEnergyLevel: Int
          _tmpEnergyLevel = _stmt.getLong(_columnIndexOfEnergyLevel).toInt()
          _item =
              ReflectionEntity(_tmpId,_tmpDateEpochDay,_tmpWentWell,_tmpDistracted,_tmpImproveTomorrow,_tmpMood,_tmpEnergyLevel)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteReflectionByDate(epochDay: Long) {
    val _sql: String = "DELETE FROM reflections WHERE dateEpochDay = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, epochDay)
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
