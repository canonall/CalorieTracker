package com.canonal.tracker_presentation.search

import com.canonal.tracker_domain.model.MealType
import com.canonal.tracker_domain.model.TrackableFood
import java.time.LocalDate

sealed class SearchEvent {
    data class OnQueryChange(val query: String) : SearchEvent()
    object OnSearch : SearchEvent()
    data class OnToggleTrackableFood(val trackableFood: TrackableFood) : SearchEvent()
    data class OnAmountForTrackableFoodChange(
        val trackableFood: TrackableFood,
        val amount: String
    ) : SearchEvent()

    data class OnTrackFoodClick(
        val trackableFood: TrackableFood,
        val mealType: MealType,
        val date: LocalDate
    ) : SearchEvent()

    data class OnSearchFocusChange(val isFocused: Boolean) : SearchEvent()
}
