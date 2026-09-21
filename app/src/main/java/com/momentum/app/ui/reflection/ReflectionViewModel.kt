package com.momentum.app.ui.reflection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momentum.app.data.repository.ReflectionRepository
import com.momentum.app.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ReflectionUiState(
    val date: LocalDate = LocalDate.now(),
    val wentWell: String = "",
    val distracted: String = "",
    val improveTomorrow: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val energyLevel: Int = 3,
    val isSaved: Boolean = false,
    val isEditingExisting: Boolean = false
)

@HiltViewModel
class ReflectionViewModel @Inject constructor(
    private val repository: ReflectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReflectionUiState())
    val uiState: StateFlow<ReflectionUiState> = _uiState.asStateFlow()

    private val today: LocalDate get() = LocalDate.now()

    init {
        viewModelScope.launch {
            repository.getReflectionForDate(today).collect { existing ->
                if (existing != null && !_uiState.value.isSaved) {
                    _uiState.update {
                        it.copy(
                            wentWell = existing.wentWell ?: "",
                            distracted = existing.distracted ?: "",
                            improveTomorrow = existing.improveTomorrow ?: "",
                            mood = existing.mood,
                            energyLevel = existing.energyLevel,
                            isEditingExisting = true
                        )
                    }
                }
            }
        }
    }

    fun onWentWellChange(value: String) = _uiState.update { it.copy(wentWell = value) }
    fun onDistractedChange(value: String) = _uiState.update { it.copy(distracted = value) }
    fun onImproveTomorrowChange(value: String) = _uiState.update { it.copy(improveTomorrow = value) }
    fun onMoodSelect(mood: Mood) = _uiState.update { it.copy(mood = mood) }
    fun onEnergyChange(level: Int) = _uiState.update { it.copy(energyLevel = level.coerceIn(1, 5)) }

    fun saveReflection() {
        viewModelScope.launch {
            val state = _uiState.value
            val entry = ReflectionEntry(
                date = today,
                wentWell = state.wentWell.trim().ifBlank { null },
                distracted = state.distracted.trim().ifBlank { null },
                improveTomorrow = state.improveTomorrow.trim().ifBlank { null },
                mood = state.mood,
                energyLevel = state.energyLevel
            )
            repository.upsertReflection(entry)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun resetSaved() = _uiState.update { it.copy(isSaved = false) }
}
