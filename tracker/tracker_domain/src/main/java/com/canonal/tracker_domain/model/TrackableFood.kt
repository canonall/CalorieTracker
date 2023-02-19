package com.canonal.tracker_domain.model

/**
 * TrackableFood can be accessed from other modules.
 * It is the model that is displayed in UI and used all over the app.
 * Response from the API mapped to TrackableFood.
 */

data class TrackableFood(
    val name: String,
    val imageUrl: String?,
    val caloriesPer100g: Int,
    val carbsPer100g: Int,
    val proteinPer100g: Int,
    val fatPer100g: Int
)
