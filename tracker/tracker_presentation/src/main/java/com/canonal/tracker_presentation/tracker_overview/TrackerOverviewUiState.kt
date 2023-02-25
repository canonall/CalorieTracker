package com.canonal.tracker_presentation.tracker_overview

import com.canonal.tracker_domain.model.TrackedFood
import java.time.LocalDate

data class TrackerOverviewUiState(
    val totalCarbs: Int = 0,
    val totalProtein: Int = 0,
    val totalFat: Int = 0,
    val totalCalories: Int = 0,
    val carbsGoal: Int = 0,
    val proteinGoal: Int = 0,
    val fatGoal: Int = 0,
    val caloriesGoal: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val trackedFoodList: List<TrackedFood> = emptyList(),
    val meals: List<Meal> = defaultMeals
)
