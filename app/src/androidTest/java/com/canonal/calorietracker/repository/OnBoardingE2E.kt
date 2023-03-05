package com.canonal.calorietracker.repository

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.canonal.calorietracker.MainActivity
import com.canonal.core.R
import com.canonal.calorietracker.navigation.Route
import com.canonal.calorietracker.ui.theme.CalorieTrackerTheme
import com.canonal.core.domain.preferences.Preferences
import com.canonal.core.domain.use_case.FilterOutDigitsUseCase
import com.canonal.onboarding_domain.use_case.age.AgeLimitUseCase
import com.canonal.onboarding_domain.use_case.height.InitialHeightUseCase
import com.canonal.onboarding_domain.use_case.nutrient_goal.ValidateNutrientsUseCase
import com.canonal.onboarding_domain.use_case.weight.FormatWeightUseCase
import com.canonal.onboarding_domain.use_case.weight.InitialWeightUseCase
import com.canonal.onboarding_domain.use_case.weight.WeightLimitUseCase
import com.canonal.onboarding_presentation.activity_level.ActivityLevelScreen
import com.canonal.onboarding_presentation.activity_level.ActivityLevelViewModel
import com.canonal.onboarding_presentation.age.AgeScreen
import com.canonal.onboarding_presentation.age.AgeViewModel
import com.canonal.onboarding_presentation.gender.GenderScreen
import com.canonal.onboarding_presentation.gender.GenderViewModel
import com.canonal.onboarding_presentation.goal_type.GoalTypeScreen
import com.canonal.onboarding_presentation.goal_type.GoalTypeViewModel
import com.canonal.onboarding_presentation.height.HeightScreen
import com.canonal.onboarding_presentation.height.HeightViewModel
import com.canonal.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.canonal.onboarding_presentation.nutrient_goal.NutrientGoalViewModel
import com.canonal.onboarding_presentation.weight.WeightScreen
import com.canonal.onboarding_presentation.weight.WeightViewModel
import com.canonal.onboarding_presentation.welcome.WelcomeScreen
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class OnBoardingE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var preferences: Preferences
    private lateinit var ageViewModel: AgeViewModel
    private lateinit var genderViewModel: GenderViewModel
    private lateinit var heightViewModel: HeightViewModel
    private lateinit var weightViewModel: WeightViewModel
    private lateinit var activityLevelViewModel: ActivityLevelViewModel
    private lateinit var goalTypeViewModel: GoalTypeViewModel
    private lateinit var nutrientsGoalViewModel: NutrientGoalViewModel
    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        ageViewModel = AgeViewModel(
            preferences = preferences,
            filterOutDigitsUseCase = FilterOutDigitsUseCase(),
            ageLimitUseCase = AgeLimitUseCase()
        )
        genderViewModel = GenderViewModel(preferences = preferences)
        heightViewModel = HeightViewModel(
            preferences = preferences,
            filterOutDigitsUseCase = FilterOutDigitsUseCase(),
            initialHeightUseCase = InitialHeightUseCase()
        )
        weightViewModel = WeightViewModel(
            preferences = preferences,
            initialWeightUseCase = InitialWeightUseCase(),
            formatWeightUseCase = FormatWeightUseCase(),
            weightLimitUseCase = WeightLimitUseCase()
        )
        activityLevelViewModel = ActivityLevelViewModel(preferences = preferences)
        goalTypeViewModel = GoalTypeViewModel(preferences = preferences)
        nutrientsGoalViewModel = NutrientGoalViewModel(
            preferences = preferences,
            filterOutDigitsUseCase = FilterOutDigitsUseCase(),
            validateNutrientsUseCase = ValidateNutrientsUseCase()
        )
        composeRule.activity.setContent {
            val scaffoldState = rememberScaffoldState()
            navController = rememberNavController()
            CalorieTrackerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) { contentPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Route.WELCOME,
                        modifier = Modifier.padding(contentPadding)
                    ) {

                        composable(route = Route.WELCOME) {
                            WelcomeScreen(onNextClick = {
                                navController.navigate(Route.GENDER)
                            })
                        }
                        composable(route = Route.GENDER) {
                            GenderScreen(onNextClick = {
                                navController.navigate(Route.AGE)
                            })
                        }
                        composable(route = Route.AGE) {
                            AgeScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.HEIGHT)
                                }
                            )
                        }
                        composable(route = Route.HEIGHT) {
                            HeightScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.WEIGHT)
                                }
                            )
                        }
                        composable(route = Route.WEIGHT) {
                            WeightScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(Route.ACTIVITY)
                                }
                            )
                        }
                        composable(route = Route.ACTIVITY) {
                            ActivityLevelScreen(onNextClick = {
                                navController.navigate(Route.GOAL)
                            })
                        }
                        composable(route = Route.GOAL) {
                            GoalTypeScreen(onNextClick = {
                                navController.navigate(Route.NUTRIENT_GOAL)
                            })
                        }
                        composable(route = Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {}
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun onBoardingNavigatesCorrectly() {

        val ageTextField = composeRule.activity.getString(R.string.age_text_field)
        val heightTextField = composeRule.activity.getString(R.string.height_text_field)
        val weightTextField = composeRule.activity.getString(R.string.weight_text_field)
        val carbsRatioTextField = composeRule.activity.getString(R.string.carbs_ratio_text_field)
        val proteinRatioTextField = composeRule.activity.getString(R.string.protein_ratio_text_field)
        val fatRatioTextField = composeRule.activity.getString(R.string.fat_ratio_text_field)

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(Route.GENDER)).isTrue()

        composeRule
            .onNodeWithText("Female")
            .assertIsDisplayed()
            .performClick()

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(Route.AGE)).isTrue()

        composeRule
            .onNodeWithContentDescription(ageTextField)
            .assertIsDisplayed()

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(Route.HEIGHT)).isTrue()

        composeRule
            .onNodeWithContentDescription(heightTextField)
            .assertIsDisplayed()

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(Route.WEIGHT)).isTrue()

        composeRule
            .onNodeWithContentDescription(weightTextField)
            .assertIsDisplayed()

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(Route.ACTIVITY)).isTrue()

        composeRule
            .onNodeWithText("Medium")
            .assertIsDisplayed()

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(Route.GOAL)).isTrue()

        composeRule
            .onNodeWithText("Keep")
            .assertIsDisplayed()

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(Route.NUTRIENT_GOAL)).isTrue()

        composeRule
            .onNodeWithContentDescription(carbsRatioTextField)
            .assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription(proteinRatioTextField)
            .assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription(fatRatioTextField)
            .assertIsDisplayed()
    }

    private fun next() {
        composeRule
            .onNodeWithText("Next")
            .assertIsDisplayed()
            .performClick()
    }
}