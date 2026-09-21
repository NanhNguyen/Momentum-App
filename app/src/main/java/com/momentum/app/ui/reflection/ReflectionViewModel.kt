package com.momentum.app.ui.reflection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momentum.app.data.repository.ReflectionRepository
import com.momentum.app.domain.model.Mood
import com.momentum.app.domain.model.ReflectionEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ReflectionDraft(
    val mood: Mood? = null,
    val energyLevel: Int = 3,
    val wentWell: String = "",
    val distracted: String = "",
    val improveTomorrow: String = ""
)

sealed class TodayReflectionState {
    /** Prompt state: no entry yet today, not editing */
    object Prompt : TodayReflectionState()

    /** Editing state: writing today's entry (either first-time or editing existing) */
    data class Editing(
        val draft: ReflectionDraft,
        val isEditingExisting: Boolean,
        val moodError: Boolean = false
    ) : TodayReflectionState()

    /** Saved state: today has a saved entry, not currently editing */
    data class Saved(val entry: ReflectionEntry) : TodayReflectionState()
}

data class ReflectionJournalUiState(
    val todayState: TodayReflectionState = TodayReflectionState.Prompt,
    val pastReflections: List<ReflectionEntry> = emptyList(),
    val todayEntry: ReflectionEntry? = null
)

@HiltViewModel
class ReflectionViewModel @Inject constructor(
    private val repository: ReflectionRepository
) : ViewModel() {

    private val today: LocalDate get() = LocalDate.now()

    // Holds manual user override for today's state (e.g. user tapped "Start writing" or "Edit")
    private val _todayStateOverride = MutableStateFlow<TodayReflectionState?>(null)

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    val uiState: StateFlow<ReflectionJournalUiState> = combine(
        repository.getAllReflectionsFlow(),
        _todayStateOverride
    ) { allReflections, stateOverride ->
        val currentToday = today
        val todayEntry = allReflections.firstOrNull { it.date == currentToday }
        val pastReflections = allReflections.filter { it.date != currentToday }

        val todayState = when {
            stateOverride != null -> stateOverride
            todayEntry != null -> TodayReflectionState.Saved(todayEntry)
            else -> TodayReflectionState.Prompt
        }

        ReflectionJournalUiState(
            todayState = todayState,
            pastReflections = pastReflections,
            todayEntry = todayEntry
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ReflectionJournalUiState()
    )

    /** Start writing today's reflection from the prompt card (first time today) */
    fun startWritingToday() {
        _todayStateOverride.value = TodayReflectionState.Editing(
            draft = ReflectionDraft(),
            isEditingExisting = false,
            moodError = false
        )
    }

    /** Start editing an existing saved entry for today */
    fun startEditingExisting() {
        val todayEntry = uiState.value.todayEntry ?: return
        _todayStateOverride.value = TodayReflectionState.Editing(
            draft = ReflectionDraft(
                mood = todayEntry.mood,
                energyLevel = todayEntry.energyLevel,
                wentWell = todayEntry.wentWell ?: "",
                distracted = todayEntry.distracted ?: "",
                improveTomorrow = todayEntry.improveTomorrow ?: ""
            ),
            isEditingExisting = true,
            moodError = false
        )
    }

    /**
     * Discard changes:
     * - If editing existing: reverts to Saved state without touching Room.
     * - If creating new from prompt: reverts to Prompt state.
     */
    fun discardChanges() {
        _todayStateOverride.value = null
    }

    fun onMoodSelect(mood: Mood) {
        val currentOverride = _todayStateOverride.value
        if (currentOverride is TodayReflectionState.Editing) {
            _todayStateOverride.value = currentOverride.copy(
                draft = currentOverride.draft.copy(mood = mood),
                moodError = false
            )
        }
    }

    fun onEnergyChange(level: Int) {
        val currentOverride = _todayStateOverride.value
        if (currentOverride is TodayReflectionState.Editing) {
            _todayStateOverride.value = currentOverride.copy(
                draft = currentOverride.draft.copy(energyLevel = level.coerceIn(1, 5))
            )
        }
    }

    fun onWentWellChange(value: String) {
        val currentOverride = _todayStateOverride.value
        if (currentOverride is TodayReflectionState.Editing) {
            _todayStateOverride.value = currentOverride.copy(
                draft = currentOverride.draft.copy(wentWell = value)
            )
        }
    }

    fun onDistractedChange(value: String) {
        val currentOverride = _todayStateOverride.value
        if (currentOverride is TodayReflectionState.Editing) {
            _todayStateOverride.value = currentOverride.copy(
                draft = currentOverride.draft.copy(distracted = value)
            )
        }
    }

    fun onImproveTomorrowChange(value: String) {
        val currentOverride = _todayStateOverride.value
        if (currentOverride is TodayReflectionState.Editing) {
            _todayStateOverride.value = currentOverride.copy(
                draft = currentOverride.draft.copy(improveTomorrow = value)
            )
        }
    }

    fun saveReflection() {
        val current = _todayStateOverride.value
        if (current !is TodayReflectionState.Editing) return

        if (current.draft.mood == null) {
            _todayStateOverride.value = current.copy(moodError = true)
            return
        }

        viewModelScope.launch {
            val existingEntry = uiState.value.todayEntry
            val entry = ReflectionEntry(
                id = existingEntry?.id ?: 0L,
                date = today,
                wentWell = current.draft.wentWell.trim().ifBlank { null },
                distracted = current.draft.distracted.trim().ifBlank { null },
                improveTomorrow = current.draft.improveTomorrow.trim().ifBlank { null },
                mood = current.draft.mood,
                energyLevel = current.draft.energyLevel
            )
            repository.upsertReflection(entry)
            // Clear override so state becomes Saved(entry) automatically from repository emission
            _todayStateOverride.value = null
            _snackbarMessage.emit("Reflection saved. Well done for taking a moment to reflect.")
        }
    }
}
