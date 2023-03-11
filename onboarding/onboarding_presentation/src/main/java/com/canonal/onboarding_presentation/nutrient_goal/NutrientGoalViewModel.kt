package com.canonal.onboarding_presentation.nutrient_goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canonal.core.domain.preferences.Preferences
import com.canonal.core.domain.use_case.FilterOutDigitsUseCase
import com.canonal.core.util.UiEvent
import com.canonal.onboarding_domain.use_case.nutrient_goal.ValidateNutrientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientGoalViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase,
    private val validateNutrientsUseCase: ValidateNutrientsUseCase
) : ViewModel() {

    var uiState by mutableStateOf(NutrientGoalUiState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    fun onEvent(nutrientGoalEvent: NutrientGoalEvent) {
        when (nutrientGoalEvent) {
            is NutrientGoalEvent.OnCarbsRatioEnter -> {
                uiState = uiState.copy(carbsRatio = filterOutDigitsUseCase(nutrientGoalEvent.ratio))
            }
            is NutrientGoalEvent.OnProteinRatioEnter -> {
                uiState =
                    uiState.copy(proteinRatio = filterOutDigitsUseCase(nutrientGoalEvent.ratio))
            }
            is NutrientGoalEvent.OnFatRatioEnter -> {
                uiState = uiState.copy(fatRatio = filterOutDigitsUseCase(nutrientGoalEvent.ratio))
            }
            NutrientGoalEvent.OnNextClick -> {
                val result = validateNutrientsUseCase(
                    carbsRatioText = uiState.carbsRatio,
                    proteinRatioText = uiState.proteinRatio,
                    fatRatioText = uiState.fatRatio
                )
                when (result) {
                    is ValidateNutrientsUseCase.NutrientValidationResult.Success -> {
                        preferences.saveCarbRatio(ratio = result.carbsRatio)
                        preferences.saveProteinRatio(ratio = result.proteinRatio)
                        preferences.saveFatRatio(ratio = result.fatRatio)
                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.Success)
                        }
                    }

                    is ValidateNutrientsUseCase.NutrientValidationResult.Error -> {
                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.ShowSnackbar(message = result.message))
                        }
                    }
                }
            }
        }
    }
}
