package com.canonal.onboarding_presentation.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canonal.core.domain.preferences.Preferences
import com.canonal.core.util.UiEvent
import com.canonal.onboarding_domain.use_case.weight.InitialWeightUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val preferences: Preferences,
    private val initialWeightUseCase: InitialWeightUseCase,
) : ViewModel() {
    var weight by mutableStateOf(getInitialWeight())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    fun onWeightEnter(newValue: String) {
        weight = newValue
    }

    fun onNextClick() {
        viewModelScope.launch {
            preferences.saveWeight(weight = weight.toInt())
            _uiEvent.send(UiEvent.Success)
        }
    }

    fun getInitialWeight(): String {
        return initialWeightUseCase(gender = preferences.loadUserInfo().gender)
    }
}