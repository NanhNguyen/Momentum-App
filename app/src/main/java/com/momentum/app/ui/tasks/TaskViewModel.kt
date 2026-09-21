package com.momentum.app.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momentum.app.data.repository.TaskRepository
import com.momentum.app.domain.model.RecurrenceRule
import com.momentum.app.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val selectedFilter: TaskFilter = TaskFilter.TODAY,
    val isLoading: Boolean = false
)

enum class TaskFilter { TODAY, THIS_WEEK, ALL }

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(TaskFilter.TODAY)
    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _filter.flatMapLatest { filter ->
                val today = LocalDate.now()
                when (filter) {
                    TaskFilter.TODAY -> repository.getTasksForDate(today)
                    TaskFilter.THIS_WEEK -> {
                        val monday = today.with(java.time.DayOfWeek.MONDAY)
                        val sunday = today.with(java.time.DayOfWeek.SUNDAY)
                        repository.getTasksForWeek(monday, sunday)
                    }
                    TaskFilter.ALL -> repository.getAllTasks()
                }
            }.collect { tasks ->
                _uiState.update { it.copy(tasks = tasks, selectedFilter = _filter.value) }
            }
        }
    }

    fun setFilter(filter: TaskFilter) {
        _filter.value = filter
    }

    fun toggleComplete(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun deleteRecurringTask(task: Task, deleteAllFuture: Boolean) {
        viewModelScope.launch {
            if (deleteAllFuture) {
                repository.deleteFutureRecurringTasks(task.title, task.date)
            } else {
                repository.deleteTask(task)
            }
        }
    }
}

// ─────────────── AddEditTask ViewModel ───────────────

data class AddEditTaskUiState(
    val title: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now(),
    val isBigThree: Boolean = false,
    val priority: Int = 0,
    val isRecurring: Boolean = false,
    val recurrenceRule: RecurrenceRule? = null,
    val isEditMode: Boolean = false,
    val isSaved: Boolean = false,
    val bigThreeLimitReached: Boolean = false, // gentle message, not error
    val showRecurringEditDialog: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditTaskUiState())
    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()

    private var editingTaskId: Long? = null
    private var editingTask: Task? = null

    fun loadTask(taskId: Long) {
        viewModelScope.launch {
            val task = repository.getTaskById(taskId) ?: return@launch
            editingTaskId = taskId
            editingTask = task
            _uiState.update {
                it.copy(
                    title = task.title,
                    description = task.description ?: "",
                    date = task.date,
                    isBigThree = task.isBigThree,
                    priority = task.priority,
                    isRecurring = task.isRecurring,
                    recurrenceRule = task.recurrenceRule,
                    isEditMode = true
                )
            }
        }
    }

    fun onTitleChange(value: String) = _uiState.update { it.copy(title = value) }
    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }
    fun onDateChange(value: LocalDate) = _uiState.update { it.copy(date = value) }
    fun onPriorityChange(value: Int) = _uiState.update { it.copy(priority = value) }

    fun onRecurrenceRuleChange(rule: RecurrenceRule?) {
        _uiState.update {
            it.copy(
                isRecurring = rule != null,
                recurrenceRule = rule
            )
        }
    }

    fun onBigThreeToggle(checked: Boolean) {
        viewModelScope.launch {
            if (checked) {
                val currentDate = _uiState.value.date
                val count = repository.getBigThreeCount(currentDate)
                // If editing, we need to not count the task itself
                val isEditing = editingTaskId != null
                val editingIsCurrentlyBigThree = isEditing &&
                        (repository.getTaskById(editingTaskId!!)?.isBigThree == true)
                val effectiveCount = if (editingIsCurrentlyBigThree) count - 1 else count
                if (effectiveCount >= 3) {
                    _uiState.update { it.copy(bigThreeLimitReached = true) }
                    return@launch
                }
            }
            _uiState.update { it.copy(isBigThree = checked, bigThreeLimitReached = false) }
        }
    }

    fun dismissBigThreeMessage() = _uiState.update { it.copy(bigThreeLimitReached = false) }

    fun onSaveClick() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please add a title to your task.") }
            return
        }
        if (editingTask?.isRecurring == true) {
            _uiState.update { it.copy(showRecurringEditDialog = true) }
        } else {
            executeSave(updateFuture = false)
        }
    }

    fun executeSave(updateFuture: Boolean) {
        val state = _uiState.value
        _uiState.update { it.copy(showRecurringEditDialog = false) }
        viewModelScope.launch {
            val task = Task(
                id = editingTaskId ?: 0L,
                title = state.title.trim(),
                description = state.description.trim().ifBlank { null },
                date = state.date,
                isBigThree = state.isBigThree,
                isCompleted = editingTask?.isCompleted ?: false,
                priority = state.priority,
                createdAt = editingTask?.createdAt ?: java.time.Instant.now(),
                isRecurring = if (!updateFuture && editingTask?.isRecurring == true) false else state.isRecurring,
                recurrenceRule = if (!updateFuture && editingTask?.isRecurring == true) null else state.recurrenceRule
            )
            if (editingTaskId != null) {
                val oldTitle = editingTask?.title ?: task.title
                if (updateFuture && editingTask?.isRecurring == true) {
                    repository.updateFutureRecurringTasks(
                        oldTitle = oldTitle,
                        newTitle = task.title,
                        newDescription = task.description,
                        newPriority = task.priority,
                        newRule = state.recurrenceRule,
                        fromDate = state.date
                    )
                }
                repository.updateTask(task)
            } else {
                repository.insertTask(task)
            }
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun dismissRecurringEditDialog() {
        _uiState.update { it.copy(showRecurringEditDialog = false) }
    }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
}
