package com.canonal.onboarding_presentation.height

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeightViewModel @Inject constructor(
    private val preferences: DefaultPreferences,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase
) : ViewModel() {
    var height by mutableStateOf("170")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    fun onHeightEnter(height: String) {
        if (height.length <= 3) {
            this.height = filterOutDigitsUseCase(text = height)
        }
    }

    fun onNextClick() {
        viewModelScope.launch {
            val heightAsInt = height.toIntOrNull() ?: kotlin.run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        message = UiText.StringResource(resId = R.string.error_height_cant_be_empty)
                    )
                )
                return@launch
            }
            preferences.saveHeight(height = heightAsInt)
            _uiEvent.send(UiEvent.Navigate(Route.WEIGHT))
        }
    }
}