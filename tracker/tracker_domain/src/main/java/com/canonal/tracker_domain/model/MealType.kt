package com.canonal.tracker_domain.model

sealed class MealType(val name: String) {
    object Breakfast : MealType("breakfast")
    object Lunch : MealType("lunch")
    object Dinner : MealType("dinner")
    object Snack : MealType("snacks")

    companion object {
        fun fromString(name: String): MealType {
            return when (name) {
                "breakfast" -> Breakfast
                "lunch" -> Lunch
                "dinner" -> Dinner
                "snacks" -> Snack
                else -> Breakfast
            }
        }
    }
}
