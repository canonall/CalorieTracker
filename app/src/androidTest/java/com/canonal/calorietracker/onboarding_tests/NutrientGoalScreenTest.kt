package com.canonal.calorietracker.onboarding_tests

import android.annotation.SuppressLint
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.canonal.calorietracker.MainActivity
import com.canonal.core.R
import com.canonal.core.domain.preferences.Preferences
import com.canonal.core.domain.use_case.FilterOutDigitsUseCase
import com.canonal.onboarding_domain.use_case.nutrient_goal.ValidateNutrientsUseCase
import com.canonal.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.canonal.onboarding_presentation.nutrient_goal.NutrientGoalViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NutrientGoalScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var preferences: Preferences
    private lateinit var nutrientGoalViewModel: NutrientGoalViewModel
    private lateinit var carbsRatioTextField: String
    private lateinit var proteinRatioTextField: String
    private lateinit var fatRatioTextField: String

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        nutrientGoalViewModel = NutrientGoalViewModel(
            preferences = preferences,
            filterOutDigitsUseCase = FilterOutDigitsUseCase(),
            validateNutrientsUseCase = ValidateNutrientsUseCase()
        )
        carbsRatioTextField = composeRule.activity.getString(R.string.carbs_ratio_text_field)
        proteinRatioTextField = composeRule.activity.getString(R.string.protein_ratio_text_field)
        fatRatioTextField = composeRule.activity.getString(R.string.fat_ratio_text_field)

        composeRule.activity.setContent {
            val scaffoldState = rememberScaffoldState()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState
            ) {
                NutrientGoalScreen(
                    scaffoldState = scaffoldState,
                    viewModel = nutrientGoalViewModel,
                    onNextClick = {}
                )
            }
        }
    }

    @Test
    fun isEmptyNutrientErrorDisplayed() {
        val emptyError = composeRule.activity.getString(R.string.error_invalid_values)

        composeRule
            .onNodeWithContentDescription(carbsRatioTextField)
            .onChildren()
            .onFirst()
            .performTextClearance()

        composeRule
            .onNodeWithText("Next")
            .performClick()

        composeRule
            .onNodeWithText(emptyError)
            .assertIsDisplayed()
    }

    @Test
    fun isTotalNot100ErrorDisplayed() {
        val totalNot100Error = composeRule.activity.getString(R.string.error_not_100_percent)

        composeRule
            .onNodeWithContentDescription(carbsRatioTextField)
            .onChildren()
            .onFirst()
            .performTextClearance()

        composeRule
            .onNodeWithContentDescription(carbsRatioTextField)
            .onChildren()
            .onFirst()
            .performTextInput("30")

        composeRule
            .onNodeWithText("Next")
            .performClick()

        composeRule
            .onNodeWithText(totalNot100Error)
            .assertIsDisplayed()
    }
}