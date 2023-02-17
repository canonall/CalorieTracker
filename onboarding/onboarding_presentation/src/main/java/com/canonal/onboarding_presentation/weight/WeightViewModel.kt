package com.canonal.onboarding_presentation.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canonal.core.data.preferences.DefaultPreferences
import com.canonal.core.util.UiEvent
import com.canonal.core.R
import com.canonal.onboarding_domain.use_case.weight.FormatWeightUseCase
import com.canonal.onboarding_domain.use_case.weight.InitialWeightUseCase
import com.canonal.core.navigation.Route
import com.canonal.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val preferences: DefaultPreferences,
    private val initialWeightUseCase: InitialWeightUseCase,
    private val formatWeightUseCase: FormatWeightUseCase
) : ViewModel() {
    var weight by mutableStateOf(getInitialWeight())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    fun onWeightEnter(newValue: String) {
        this.weight = formatWeightUseCase.invoke(displayedText = this.weight, newValue = newValue)
    }

    fun onNextClick() {
        viewModelScope.launch {
            val weightAsFloat = weight.toFloatOrNull() ?: kotlin.run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        message = UiText.StringResource(resId = R.string.error_weight_cant_be_empty)
                    )
                )
                return@launch
            }
            preferences.saveWeight(weight = weightAsFloat)
            _uiEvent.send(UiEvent.Navigate(Route.ACTIVITY))
        }
    }

    private fun getInitialWeight(): String {
        return initialWeightUseCase.invoke(preferences.loadUserInfo().gender)
    }
}