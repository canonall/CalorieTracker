package com.canonal.onboarding_presentation.age

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canonal.core.data.preferences.DefaultPreferences
import com.canonal.core.domain.use_case.FilterOutDigitsUseCase
import com.canonal.core.util.UiEvent
import com.canonal.core.R
import com.canonal.core.navigation.Route
import com.canonal.core.util.UiText
import com.canonal.onboarding_domain.use_case.age.AgeLimitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgeViewModel @Inject constructor(
    private val preferences: DefaultPreferences,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase,
    private val ageLimitUseCase: AgeLimitUseCase
) : ViewModel() {
    var age by mutableStateOf("20")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    fun onAgeEnter(age: String) {
        if (age.length <= 3) {
            this.age = filterOutDigitsUseCase(text = age)
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            val ageAsInt = age.toIntOrNull() ?: kotlin.run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        message = UiText.StringResource(resId = R.string.error_age_cant_be_empty)
                    )
                )
                return@launch
            }
            if (ageLimitUseCase.invoke(ageAsInt)) {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        message = UiText.StringResource(resId = R.string.error_age_limit)
                    )
                )
                return@launch
            }
            preferences.saveAge(age = ageAsInt)
            _uiEvent.send(UiEvent.Navigate(Route.HEIGHT))
        }
    }
}