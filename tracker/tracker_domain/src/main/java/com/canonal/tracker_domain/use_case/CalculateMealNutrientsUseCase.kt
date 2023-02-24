package com.canonal.tracker_domain.use_case

import com.canonal.core.domain.model.ActivityLevel
import com.canonal.core.domain.model.Gender
import com.canonal.core.domain.model.GoalType
import com.canonal.core.domain.model.UserInfo
import com.canonal.core.domain.preferences.Preferences
import com.canonal.tracker_domain.model.MealType
import com.canonal.tracker_domain.model.TrackedFood
import kotlin.math.roundToInt

class CalculateMealNutrientsUseCase(private val preferences: Preferences) {
    operator fun invoke(trackedFoodList: List<TrackedFood>): MealNutrientsResult {
        val allNutrients = trackedFoodList
            .groupBy { trackedFood -> trackedFood.mealType }
            .mapValues { entry ->
                val mealType = entry.key
                val foods = entry.value

                // calculate total amounts taken for a mealType
                val carbsSum = foods.sumOf { trackedFood -> trackedFood.carbs }
                val proteinSum = foods.sumOf { trackedFood -> trackedFood.protein }
                val fatSum = foods.sumOf { trackedFood -> trackedFood.fat }
                val caloriesSum = foods.sumOf { trackedFood -> trackedFood.calories }

                MealNutrients(
                    carbs = carbsSum,
                    protein = proteinSum,
                    fat = fatSum,
                    calories = caloriesSum,
                    mealType = mealType
                )
            }

        // calculate total amounts taken for a day
        val totalCarbs = allNutrients.values.sumOf { mealNutrients -> mealNutrients.carbs }
        val totalProtein = allNutrients.values.sumOf { mealNutrients -> mealNutrients.protein }
        val totalFat = allNutrients.values.sumOf { mealNutrients -> mealNutrients.fat }
        val totalCalories = allNutrients.values.sumOf { mealNutrients -> mealNutrients.calories }

        // information entered during onboarding
        val userInfo = preferences.loadUserInfo()

        // calculate goals based on the percentage user entered during onboarding
        val caloriesGoal = dailyCalorieRequirement(userInfo)
        val carbsGoal = (caloriesGoal * userInfo.carbRatio / 4f).roundToInt()
        val proteinGoal = (caloriesGoal * userInfo.proteinRatio / 4f).roundToInt()
        val fatGoal = (caloriesGoal * userInfo.fatRatio / 9f).roundToInt()

        return MealNutrientsResult(
            carbsGoal = carbsGoal,
            proteinGoal = proteinGoal,
            fatGoal = fatGoal,
            caloriesGoal = caloriesGoal,
            totalCarbs = totalCarbs,
            totalProtein = totalProtein,
            totalFat = totalFat,
            totalCalories = totalCalories,
            mealNutrientsMap = allNutrients
        )
    }

    data class MealNutrients(
        val carbs: Int,
        val protein: Int,
        val fat: Int,
        val calories: Int,
        val mealType: MealType
    )

    data class MealNutrientsResult(
        val carbsGoal: Int,
        val proteinGoal: Int,
        val fatGoal: Int,
        val caloriesGoal: Int,
        val totalCarbs: Int,
        val totalProtein: Int,
        val totalFat: Int,
        val totalCalories: Int,
        val mealNutrientsMap: Map<MealType, MealNutrients>
    )

    private fun dailyCalorieRequirement(userInfo: UserInfo): Int {
        val activityFactor = when (userInfo.activityLevel) {
            is ActivityLevel.Low -> 1.2f
            is ActivityLevel.Medium -> 1.3f
            is ActivityLevel.High -> 1.4f
        }
        val caloryExtra = when (userInfo.goalType) {
            is GoalType.LoseWeight -> -500
            is GoalType.KeepWeight -> 0
            is GoalType.GainWeight -> 500
        }
        return (bmr(userInfo) * activityFactor + caloryExtra).roundToInt()
    }

    // basal metabolic rate
    private fun bmr(userInfo: UserInfo): Int {
        return when (userInfo.gender) {
            is Gender.Male -> {
                (66.47f + 13.75f * userInfo.weight +
                        5f * userInfo.height - 6.75f * userInfo.age).roundToInt()
            }
            is Gender.Female -> {
                (665.09f + 9.56f * userInfo.weight +
                        1.84f * userInfo.height - 4.67 * userInfo.age).roundToInt()
            }
        }
    }
}
