package com.canonal.tracker_data.mapper

import com.canonal.tracker_data.local.entity.TrackedFoodEntity
import com.canonal.tracker_domain.model.MealType
import com.canonal.tracker_domain.model.TrackedFood
import java.time.LocalDate

fun TrackedFoodEntity.toTrackedFood(): TrackedFood {
    return TrackedFood(
        id = id,
        name = name,
        imageUrl = imageUrl,
        carbs = carbs,
        protein = protein,
        fat = fat,
        calories = calories,
        mealType = MealType.fromString(type),
        amount = amount,
        date = LocalDate.of(year, month, dayOfMonth)
    )
}

fun TrackedFood.toTrackedFoodEntity(): TrackedFoodEntity {
    return TrackedFoodEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        carbs = carbs,
        protein = protein,
        fat = fat,
        calories = calories,
        type = mealType.name,
        amount = amount,
        dayOfMonth = date.dayOfMonth,
        month = date.monthValue,
        year = date.year
    )
}
