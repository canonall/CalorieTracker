package com.canonal.tracker_domain.use_case

import com.canonal.core.domain.model.ActivityLevel
import com.canonal.core.domain.model.Gender
import com.canonal.core.domain.model.GoalType
import com.canonal.core.domain.model.UserInfo
import com.canonal.core.domain.preferences.Preferences
import com.canonal.tracker_domain.model.MealType
import com.canonal.tracker_domain.model.TrackedFood
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random

class CalculateMealNutrientsUseCaseTest {

    private lateinit var calculateMealNutrientsUseCase: CalculateMealNutrientsUseCase

    @Before
    fun setUp() {
        /**
         * Every preferences.loadUserInfo() call will return UserInfo instance
         */
        val preferences = mockk<Preferences>(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 180,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )
        calculateMealNutrientsUseCase = CalculateMealNutrientsUseCase(preferences)
    }

    @Test
    fun `Calories for breakfast properly calculated `() {
        val trackedFoodList = (1..30).map {
            TrackedFood(
                name = "name",
                calories = Random.nextInt(2000),
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now()
            )
        }

        val result = calculateMealNutrientsUseCase(trackedFoodList = trackedFoodList)

        val breakfastCalories = result.mealNutrientsMap.values
            .filter { mealNutrients ->
                mealNutrients.mealType is MealType.Breakfast
            }
            .sumOf { mealNutrients ->
                mealNutrients.calories
            }
        val expectedCalories = trackedFoodList
            .filter { mealNutrients ->
                mealNutrients.mealType is MealType.Breakfast
            }
            .sumOf { mealNutrients ->
                mealNutrients.calories
            }

        assertThat(breakfastCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Calories for lunch properly calculated `() {
        val trackedFoodList = (1..30).map {
            TrackedFood(
                name = "name",
                calories = Random.nextInt(2000),
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now()
            )
        }

        val result = calculateMealNutrientsUseCase(trackedFoodList = trackedFoodList)

        val lunchCalories = result.mealNutrientsMap.values
            .filter { mealNutrients ->
                mealNutrients.mealType is MealType.Lunch
            }
            .sumOf { mealNutrients ->
                mealNutrients.calories
            }
        val expectedCalories = trackedFoodList
            .filter { mealNutrients ->
                mealNutrients.mealType is MealType.Lunch
            }
            .sumOf { mealNutrients ->
                mealNutrients.calories
            }

        assertThat(lunchCalories).isEqualTo(expectedCalories)
    }
}