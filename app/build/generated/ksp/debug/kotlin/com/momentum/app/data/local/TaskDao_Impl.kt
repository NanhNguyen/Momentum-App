package com.momentum.app.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class TaskDao_Impl(
  __db: RoomDatabase,
) : TaskDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTaskEntity: EntityInsertAdapter<TaskEntity>

  private val __deleteAdapterOfTaskEntity: EntityDeleteOrUpdateAdapter<TaskEntity>

  private val __updateAdapterOfTaskEntity: EntityDeleteOrUpdateAdapter<TaskEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTaskEntity = object : EntityInsertAdapter<TaskEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `tasks` (`id`,`title`,`description`,`dateEpochDay`,`isBigThree`,`isCompleted`,`priority`,`createdAtEpochMilli`,`isRecurring`,`recurrenceRule`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TaskEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        statement.bindLong(4, entity.dateEpochDay)
        val _tmp: Int = if (entity.isBigThree) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        val _tmp_1: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(6, _tmp_1.toLong())
        statement.bindLong(7, entity.priority.toLong())
        statement.bindLong(8, entity.createdAtEpochMilli)
        val _tmp_2: Int = if (entity.isRecurring) 1 else 0
        statement.bindLong(9, _tmp_2.toLong())
        val _tmpRecurrenceRule: String? = entity.recurrenceRule
        if (_tmpRecurrenceRule == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpRecurrenceRule)
        }
      }
    }
    this.__deleteAdapterOfTaskEntity = object : EntityDeleteOrUpdateAdapter<TaskEntity>() {
      protected override fun createQuery(): String = "DELETE FROM `tasks` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TaskEntity) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfTaskEntity = object : EntityDeleteOrUpdateAdapter<TaskEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `tasks` SET `id` = ?,`title` = ?,`description` = ?,`dateEpochDay` = ?,`isBigThree` = ?,`isCompleted` = ?,`priority` = ?,`createdAtEpochMilli` = ?,`isRecurring` = ?,`recurrenceRule` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TaskEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.title)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        statement.bindLong(4, entity.dateEpochDay)
        val _tmp: Int = if (entity.isBigThree) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        val _tmp_1: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(6, _tmp_1.toLong())
        statement.bindLong(7, entity.priority.toLong())
        statement.bindLong(8, entity.createdAtEpochMilli)
        val _tmp_2: Int = if (entity.isRecurring) 1 else 0
        statement.bindLong(9, _tmp_2.toLong())
        val _tmpRecurrenceRule: String? = entity.recurrenceRule
        if (_tmpRecurrenceRule == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpRecurrenceRule)
        }
        statement.bindLong(11, entity.id)
      }
    }
  }

  public override suspend fun insertTask(task: TaskEntity): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfTaskEntity.insertAndReturnId(_connection, task)
    _result
  }

  public override suspend fun deleteTask(task: TaskEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfTaskEntity.handle(_connection, task)
  }

  public override suspend fun updateTask(task: TaskEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfTaskEntity.handle(_connection, task)
  }

  public override fun getAllTasks(): Flow<List<TaskEntity>> {
    val _sql: String = "SELECT * FROM tasks ORDER BY dateEpochDay DESC, createdAtEpochMilli DESC"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfIsBigThree: Int = getColumnIndexOrThrow(_stmt, "isBigThree")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrenceRule")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpIsBigThree: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsBigThree).toInt()
          _tmpIsBigThree = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          val _tmpIsRecurring: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_2 != 0
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_columnIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_columnIndexOfRecurrenceRule)
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDateEpochDay,_tmpIsBigThree,_tmpIsCompleted,_tmpPriority,_tmpCreatedAtEpochMilli,_tmpIsRecurring,_tmpRecurrenceRule)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTasksForDate(epochDay: Long): Flow<List<TaskEntity>> {
    val _sql: String =
        "SELECT * FROM tasks WHERE dateEpochDay = ? ORDER BY isBigThree DESC, priority DESC"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, epochDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfIsBigThree: Int = getColumnIndexOrThrow(_stmt, "isBigThree")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrenceRule")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpIsBigThree: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsBigThree).toInt()
          _tmpIsBigThree = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          val _tmpIsRecurring: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_2 != 0
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_columnIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_columnIndexOfRecurrenceRule)
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDateEpochDay,_tmpIsBigThree,_tmpIsCompleted,_tmpPriority,_tmpCreatedAtEpochMilli,_tmpIsRecurring,_tmpRecurrenceRule)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getBigThreeForDate(epochDay: Long): Flow<List<TaskEntity>> {
    val _sql: String =
        "SELECT * FROM tasks WHERE dateEpochDay = ? AND isBigThree = 1 ORDER BY createdAtEpochMilli ASC"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, epochDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfIsBigThree: Int = getColumnIndexOrThrow(_stmt, "isBigThree")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrenceRule")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpIsBigThree: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsBigThree).toInt()
          _tmpIsBigThree = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          val _tmpIsRecurring: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_2 != 0
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_columnIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_columnIndexOfRecurrenceRule)
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDateEpochDay,_tmpIsBigThree,_tmpIsCompleted,_tmpPriority,_tmpCreatedAtEpochMilli,_tmpIsRecurring,_tmpRecurrenceRule)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBigThreeCountForDate(epochDay: Long): Int {
    val _sql: String = "SELECT COUNT(*) FROM tasks WHERE dateEpochDay = ? AND isBigThree = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, epochDay)
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTaskById(id: Long): TaskEntity? {
    val _sql: String = "SELECT * FROM tasks WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfIsBigThree: Int = getColumnIndexOrThrow(_stmt, "isBigThree")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrenceRule")
        val _result: TaskEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpIsBigThree: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsBigThree).toInt()
          _tmpIsBigThree = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          val _tmpIsRecurring: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_2 != 0
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_columnIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_columnIndexOfRecurrenceRule)
          }
          _result =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDateEpochDay,_tmpIsBigThree,_tmpIsCompleted,_tmpPriority,_tmpCreatedAtEpochMilli,_tmpIsRecurring,_tmpRecurrenceRule)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getTasksForWeek(startEpochDay: Long, endEpochDay: Long):
      Flow<List<TaskEntity>> {
    val _sql: String = """
        |
        |        SELECT * FROM tasks 
        |        WHERE dateEpochDay >= ? AND dateEpochDay <= ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, startEpochDay)
        _argIndex = 2
        _stmt.bindLong(_argIndex, endEpochDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfIsBigThree: Int = getColumnIndexOrThrow(_stmt, "isBigThree")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrenceRule")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpIsBigThree: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsBigThree).toInt()
          _tmpIsBigThree = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          val _tmpIsRecurring: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_2 != 0
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_columnIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_columnIndexOfRecurrenceRule)
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDateEpochDay,_tmpIsBigThree,_tmpIsCompleted,_tmpPriority,_tmpCreatedAtEpochMilli,_tmpIsRecurring,_tmpRecurrenceRule)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRecurringTasks(): Flow<List<TaskEntity>> {
    val _sql: String = "SELECT * FROM tasks WHERE isRecurring = 1"
    return createFlow(__db, false, arrayOf("tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfIsBigThree: Int = getColumnIndexOrThrow(_stmt, "isBigThree")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrenceRule")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpIsBigThree: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsBigThree).toInt()
          _tmpIsBigThree = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          val _tmpIsRecurring: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_2 != 0
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_columnIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_columnIndexOfRecurrenceRule)
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDateEpochDay,_tmpIsBigThree,_tmpIsCompleted,_tmpPriority,_tmpCreatedAtEpochMilli,_tmpIsRecurring,_tmpRecurrenceRule)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getRecurringTasksSync(): List<TaskEntity> {
    val _sql: String = "SELECT * FROM tasks WHERE isRecurring = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfIsBigThree: Int = getColumnIndexOrThrow(_stmt, "isBigThree")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrenceRule")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpIsBigThree: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsBigThree).toInt()
          _tmpIsBigThree = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          val _tmpIsRecurring: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_2 != 0
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_columnIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_columnIndexOfRecurrenceRule)
          }
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDateEpochDay,_tmpIsBigThree,_tmpIsCompleted,_tmpPriority,_tmpCreatedAtEpochMilli,_tmpIsRecurring,_tmpRecurrenceRule)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTaskByTitleAndDate(title: String, epochDay: Long): TaskEntity? {
    val _sql: String = "SELECT * FROM tasks WHERE title = ? AND dateEpochDay = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, title)
        _argIndex = 2
        _stmt.bindLong(_argIndex, epochDay)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateEpochDay: Int = getColumnIndexOrThrow(_stmt, "dateEpochDay")
        val _columnIndexOfIsBigThree: Int = getColumnIndexOrThrow(_stmt, "isBigThree")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfCreatedAtEpochMilli: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMilli")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfRecurrenceRule: Int = getColumnIndexOrThrow(_stmt, "recurrenceRule")
        val _result: TaskEntity?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpDateEpochDay: Long
          _tmpDateEpochDay = _stmt.getLong(_columnIndexOfDateEpochDay)
          val _tmpIsBigThree: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsBigThree).toInt()
          _tmpIsBigThree = _tmp != 0
          val _tmpIsCompleted: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp_1 != 0
          val _tmpPriority: Int
          _tmpPriority = _stmt.getLong(_columnIndexOfPriority).toInt()
          val _tmpCreatedAtEpochMilli: Long
          _tmpCreatedAtEpochMilli = _stmt.getLong(_columnIndexOfCreatedAtEpochMilli)
          val _tmpIsRecurring: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp_2 != 0
          val _tmpRecurrenceRule: String?
          if (_stmt.isNull(_columnIndexOfRecurrenceRule)) {
            _tmpRecurrenceRule = null
          } else {
            _tmpRecurrenceRule = _stmt.getText(_columnIndexOfRecurrenceRule)
          }
          _result =
              TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpDateEpochDay,_tmpIsBigThree,_tmpIsCompleted,_tmpPriority,_tmpCreatedAtEpochMilli,_tmpIsRecurring,_tmpRecurrenceRule)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteTaskById(id: Long) {
    val _sql: String = "DELETE FROM tasks WHERE id = ?"
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

  public override suspend fun deleteFutureRecurringTasks(title: String, fromEpochDay: Long) {
    val _sql: String = "DELETE FROM tasks WHERE title = ? AND isRecurring = 1 AND dateEpochDay >= ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, title)
        _argIndex = 2
        _stmt.bindLong(_argIndex, fromEpochDay)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateFutureRecurringTasks(
    oldTitle: String,
    newTitle: String,
    newDescription: String?,
    newPriority: Int,
    newRule: String?,
    fromEpochDay: Long,
  ) {
    val _sql: String = """
        |
        |        UPDATE tasks 
        |        SET title = ?, description = ?, priority = ?, recurrenceRule = ? 
        |        WHERE title = ? AND isRecurring = 1 AND dateEpochDay >= ?
        |    
        """.trimMargin()
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, newTitle)
        _argIndex = 2
        if (newDescription == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, newDescription)
        }
        _argIndex = 3
        _stmt.bindLong(_argIndex, newPriority.toLong())
        _argIndex = 4
        if (newRule == null) {
          _stmt.bindNull(_argIndex)
        } else {
          _stmt.bindText(_argIndex, newRule)
        }
        _argIndex = 5
        _stmt.bindText(_argIndex, oldTitle)
        _argIndex = 6
        _stmt.bindLong(_argIndex, fromEpochDay)
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
