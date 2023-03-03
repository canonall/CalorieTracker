package com.canonal.tracker_presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canonal.core.R
import com.canonal.core.domain.use_case.FilterOutDigitsUseCase
import com.canonal.core.util.UiEvent
import com.canonal.core.util.UiText
import com.canonal.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    fun onEvent(searchEvent: SearchEvent) {
        when (searchEvent) {
            is SearchEvent.OnAmountForTrackableFoodChange -> {
                state =
                    state.copy(trackableFoodStateList = state.trackableFoodStateList.map { trackableFoodUiState ->
                        if (trackableFoodUiState.trackableFood == searchEvent.trackableFood) {
                            trackableFoodUiState.copy(amount = filterOutDigitsUseCase(text = searchEvent.amount))
                        } else trackableFoodUiState
                    })
            }
            is SearchEvent.OnQueryChange -> {
                state = state.copy(query = searchEvent.query)
            }
            SearchEvent.OnSearch -> {
                executeSearch()
            }
            is SearchEvent.OnSearchFocusChange -> {
                state = state.copy(isHintVisible = !searchEvent.isFocused && state.query.isBlank())
            }
            is SearchEvent.OnToggleTrackableFood -> {
                state =
                    state.copy(trackableFoodStateList = state.trackableFoodStateList.map { trackableFoodUiState ->
                        if (trackableFoodUiState.trackableFood == searchEvent.trackableFood) {
                            trackableFoodUiState.copy(isExpanded = !trackableFoodUiState.isExpanded)
                        } else trackableFoodUiState
                    })
            }

            is SearchEvent.OnTrackFoodClick -> {
                trackFood(searchEvent = searchEvent)
            }
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            state = state.copy(
                isSearching = true,
                trackableFoodStateList = emptyList()
            )
            trackerUseCases
                .searchFoodUseCase(query = state.query)
                .onSuccess { trackableFoodList ->
                    state = state.copy(
                        trackableFoodStateList = trackableFoodList.map { trackableFood ->
                            TrackableFoodUiState(trackableFood = trackableFood)
                        },
                        isSearching = false,
                        query = ""
                    )
                }
                .onFailure {
                    state = state.copy(isSearching = false)
                    _uiEvent.send(UiEvent.ShowSnackbar(message = UiText.StringResource(R.string.error_something_went_wrong)))
                }
        }
    }

    private fun trackFood(searchEvent: SearchEvent.OnTrackFoodClick) {
        viewModelScope.launch {
            val clickedItemUiState = state.trackableFoodStateList.find { trackableFoodUiState ->
                trackableFoodUiState.trackableFood == searchEvent.trackableFood
            }
            trackerUseCases.trackFoodUseCase(
                trackableFood = clickedItemUiState?.trackableFood ?: return@launch,
                amount = clickedItemUiState.amount.toIntOrNull() ?: return@launch,
                mealType = searchEvent.mealType,
                date = searchEvent.date
            )
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
}
