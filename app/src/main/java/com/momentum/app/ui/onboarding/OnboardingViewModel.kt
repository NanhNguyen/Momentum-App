package com.momentum.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momentum.app.data.repository.HabitRepository
import com.momentum.app.data.repository.SettingsRepository
import com.momentum.app.domain.model.Habit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val currentPage: Int = 0,
    val selectedHabitTemplates: Set<String> = emptySet(),
    val isCompleted: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    val habitSuggestions = listOf(
        "Daily Exercise" to "Move your body for at least 20 minutes",
        "Read 10 Pages" to "Read a good book to expand your mind",
        "Drink 2L Water" to "Stay hydrated throughout the day",
        "Sleep Early" to "Wind down and rest by 11:00 PM"
    )

    fun toggleHabitSuggestion(title: String) {
        val current = _uiState.value.selectedHabitTemplates.toMutableSet()
        if (current.contains(title)) {
            current.remove(title)
        } else {
            current.add(title)
        }
        _uiState.value = _uiState.value.copy(selectedHabitTemplates = current)
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            val selected = _uiState.value.selectedHabitTemplates
            habitSuggestions.forEach { (name, desc) ->
                if (selected.contains(name)) {
                    habitRepository.insertHabit(
                        Habit(
                            name = name,
                            description = desc,
                            frequencyPerWeek = 7
                        )
                    )
                }
            }
            settingsRepository.updateOnboardingCompleted(true)
            _uiState.value = _uiState.value.copy(isCompleted = true)
        }
    }

    fun skipOnboarding() {
        viewModelScope.launch {
            settingsRepository.updateOnboardingCompleted(true)
            _uiState.value = _uiState.value.copy(isCompleted = true)
        }
    }
}
