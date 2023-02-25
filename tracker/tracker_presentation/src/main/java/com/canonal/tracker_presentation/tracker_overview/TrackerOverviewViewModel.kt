package com.canonal.tracker_presentation.tracker_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canonal.core.domain.preferences.Preferences
import com.canonal.core.navigation.Route
import com.canonal.core.util.UiEvent
import com.canonal.tracker_domain.use_case.TrackerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    preferences: Preferences,
    private val trackerUseCases: TrackerUseCases
) : ViewModel() {

    init {
        preferences.saveShouldShowOnboarding(shouldShowOnboarding = false)
    }

    var uiState by mutableStateOf(TrackerOverviewUiState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent>
        get() = _uiEvent.receiveAsFlow()

    private var getFoodsForDateJob: Job? = null

    fun onEvent(trackerOverviewEvent: TrackerOverviewEvent) {
        when (trackerOverviewEvent) {
            is TrackerOverviewEvent.OnAddFoodClick -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        UiEvent.Navigate(
                            route = Route.SEARCH
                                    + "/${trackerOverviewEvent.meal.name}"
                                    + "/${uiState.date.dayOfMonth}"
                                    + "/${uiState.date.monthValue}"
                                    + "/${uiState.date.year}"
                        )
                    )
                }
            }
            is TrackerOverviewEvent.OnDeleteTrackedFoodClick -> {
                viewModelScope.launch {
                    trackerUseCases.deleteTrackedFoodUseCase(trackedFood = trackerOverviewEvent.trackedFood)
                    refreshFoods()
                }
            }
            TrackerOverviewEvent.OnNextDayClick -> {
                uiState = uiState.copy(date = uiState.date.plusDays(1))
                refreshFoods()
            }
            TrackerOverviewEvent.OnPreviousDayClick -> {
                uiState = uiState.copy(date = uiState.date.minusDays(1))
                refreshFoods()

            }
            is TrackerOverviewEvent.OnToggleMealClick -> {
                uiState = uiState.copy(
                    meals = uiState.meals.map { meal ->
                        if (meal.name == trackerOverviewEvent.meal.name) {
                            meal.copy(isExpanded = !meal.isExpanded)
                        } else meal
                    }
                )
            }
        }
    }

    private fun refreshFoods() {
        getFoodsForDateJob?.cancel()
        getFoodsForDateJob = trackerUseCases
            .getFoodsForDateUseCase(uiState.date)
            .onEach { trackedFoodList ->
                val nutrientsResult =
                    trackerUseCases.calculateMealNutrientsUseCase(trackedFoodList = trackedFoodList)
                uiState = uiState.copy(
                    totalCarbs = nutrientsResult.totalCarbs,
                    totalProtein = nutrientsResult.totalProtein,
                    totalFat = nutrientsResult.totalFat,
                    totalCalories = nutrientsResult.totalCalories,
                    carbsGoal = nutrientsResult.carbsGoal,
                    proteinGoal = nutrientsResult.proteinGoal,
                    fatGoal = nutrientsResult.fatGoal,
                    caloriesGoal = nutrientsResult.caloriesGoal,
                    trackedFoodList = trackedFoodList,
                    meals = uiState.meals.map { meal ->
                        val nutrientsForMeal = nutrientsResult.mealNutrientsMap[meal.mealType]
                            ?: return@map meal.copy(
                                carbs = 0,
                                protein = 0,
                                fat = 0,
                                calories = 0
                            )
                        meal.copy(
                            carbs = nutrientsForMeal.carbs,
                            protein = nutrientsForMeal.protein,
                            fat = nutrientsForMeal.fat,
                            calories = nutrientsForMeal.calories
                        )
                    }
                )
            }
            .launchIn(viewModelScope)
    }
}