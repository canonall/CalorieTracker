package com.canonal.tracker_domain.model

import java.time.LocalDate

/**
 * TrackedFood can be accessed from other modules.
 * It is the model that is displayed in UI and used all over the app.
 * Response from the API mapped to TrackedFood.
 */

data class TrackedFood(
    val id: Int? = null,
    val name: String,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val imageUrl: String?,
    val mealType: MealType,
    val amount: Int,
    val date: LocalDate,
    val calories: Int
)