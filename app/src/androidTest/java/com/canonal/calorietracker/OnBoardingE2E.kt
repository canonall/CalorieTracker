package com.canonal.calorietracker

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.canonal.calorietracker.navigation.CoreFeatureNavigator
import com.canonal.calorietracker.ui.theme.CalorieTrackerTheme
import com.canonal.core.R
import com.canonal.core.domain.preferences.Preferences
import com.canonal.core.domain.use_case.FilterOutDigitsUseCase
import com.canonal.onboarding_domain.use_case.age.AgeLimitUseCase
import com.canonal.onboarding_domain.use_case.height.InitialHeightUseCase
import com.canonal.onboarding_domain.use_case.nutrient_goal.ValidateNutrientsUseCase
import com.canonal.onboarding_domain.use_case.weight.FormatWeightUseCase
import com.canonal.onboarding_domain.use_case.weight.InitialWeightUseCase
import com.canonal.onboarding_domain.use_case.weight.WeightLimitUseCase
import com.canonal.onboarding_presentation.OnboardingNavGraph
import com.canonal.onboarding_presentation.activity_level.ActivityLevelScreen
import com.canonal.onboarding_presentation.activity_level.ActivityLevelViewModel
import com.canonal.onboarding_presentation.age.AgeScreen
import com.canonal.onboarding_presentation.age.AgeViewModel
import com.canonal.onboarding_presentation.destinations.*
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
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.require
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
                    DestinationsNavHost(
                        navGraph = OnboardingNavGraph,
                        navController = navController,
                        modifier = Modifier.padding(contentPadding),
                        dependenciesContainerBuilder = {
                            dependency(scaffoldState)
                            dependency(
                                CoreFeatureNavigator(
                                    currentDestination = destination,
                                    navController = navController
                                )
                            )
                        }
                    ) {
                        composable(WelcomeScreenDestination) {
                            WelcomeScreen(navigator = buildDependencies().require())
                        }
                        composable(GenderScreenDestination) {
                            GenderScreen(
                                navigator = buildDependencies().require(),
                                viewModel = genderViewModel
                            )
                        }
                        composable(AgeScreenDestination) {
                            AgeScreen(
                                navigator = buildDependencies().require(),
                                scaffoldState = buildDependencies().require(),
                                viewModel = ageViewModel
                            )
                        }
                        composable(HeightScreenDestination) {
                            HeightScreen(
                                scaffoldState = buildDependencies().require(),
                                navigator = buildDependencies().require(),
                                viewModel = heightViewModel
                            )
                        }
                        composable(WeightScreenDestination) {
                            WeightScreen(
                                scaffoldState = buildDependencies().require(),
                                navigator = buildDependencies().require(),
                                viewModel = weightViewModel
                            )
                        }
                        composable(ActivityLevelScreenDestination) {
                            ActivityLevelScreen(
                                navigator = buildDependencies().require(),
                                viewModel = activityLevelViewModel
                            )
                        }
                        composable(GoalTypeScreenDestination) {
                            GoalTypeScreen(
                                navigator = buildDependencies().require(),
                                viewModel = goalTypeViewModel
                            )
                        }
                        composable(NutrientGoalScreenDestination) {
                            NutrientGoalScreen(
                                navigator = buildDependencies().require(),
                                scaffoldState = buildDependencies().require(),
                                viewModel = nutrientsGoalViewModel
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
        val proteinRatioTextField =
            composeRule.activity.getString(R.string.protein_ratio_text_field)
        val fatRatioTextField = composeRule.activity.getString(R.string.fat_ratio_text_field)

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(GenderScreenDestination.route))
            .isTrue()

        composeRule
            .onNodeWithText("Female")
            .assertIsDisplayed()
            .performClick()

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(AgeScreenDestination.route))
            .isTrue()

        composeRule
            .onNodeWithContentDescription(ageTextField)
            .assertIsDisplayed()

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(HeightScreenDestination.route))
            .isTrue()

        composeRule
            .onNodeWithContentDescription(heightTextField)
            .assertIsDisplayed()

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(WeightScreenDestination.route))
            .isTrue()

        composeRule
            .onNodeWithContentDescription(weightTextField)
            .assertIsDisplayed()

        next()

        Truth.assertThat(
            navController.currentDestination?.route?.equals(
                ActivityLevelScreenDestination.route
            )
        ).isTrue()

        composeRule
            .onNodeWithText("Medium")
            .assertIsDisplayed()

        next()

        Truth.assertThat(navController.currentDestination?.route?.equals(GoalTypeScreenDestination.route))
            .isTrue()

        composeRule
            .onNodeWithText("Keep")
            .assertIsDisplayed()

        next()

        Truth.assertThat(
            navController.currentDestination?.route?.equals(
                NutrientGoalScreenDestination.route
            )
        )
            .isTrue()

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
