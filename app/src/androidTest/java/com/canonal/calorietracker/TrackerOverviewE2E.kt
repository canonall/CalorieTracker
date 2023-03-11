package com.canonal.calorietracker

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.canonal.calorietracker.navigation.CoreFeatureNavigator
import com.canonal.calorietracker.repository.TrackerRepositoryFake
import com.canonal.calorietracker.ui.theme.CalorieTrackerTheme
import com.canonal.core.domain.model.ActivityLevel
import com.canonal.core.domain.model.Gender
import com.canonal.core.domain.model.GoalType
import com.canonal.core.domain.model.UserInfo
import com.canonal.core.domain.preferences.Preferences
import com.canonal.core.domain.use_case.FilterOutDigitsUseCase
import com.canonal.tracker_domain.model.TrackableFood
import com.canonal.tracker_domain.use_case.*
import com.canonal.tracker_presentation.TrackerOverviewNavGraph
import com.canonal.tracker_presentation.destinations.SearchScreenDestination
import com.canonal.tracker_presentation.destinations.TrackerOverviewScreenDestination
import com.canonal.tracker_presentation.search.SearchScreen
import com.canonal.tracker_presentation.search.SearchViewModel
import com.canonal.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.canonal.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import com.google.common.truth.Truth.assertThat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.navigation.require
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@HiltAndroidTest
class TrackerOverviewE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repositoryFake: TrackerRepositoryFake
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
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
        repositoryFake = TrackerRepositoryFake()
        trackerUseCases =
            TrackerUseCases(
                trackFoodUseCase = TrackFoodUseCase(trackerRepository = repositoryFake),
                searchFoodUseCase = SearchFoodUseCase(trackerRepository = repositoryFake),
                getFoodsForDateUseCase = GetFoodsForDateUseCase(trackerRepository = repositoryFake),
                deleteTrackedFoodUseCase = DeleteTrackedFoodUseCase(trackerRepository = repositoryFake),
                calculateMealNutrientsUseCase = CalculateMealNutrientsUseCase(preferences = preferences)
            )
        trackerOverviewViewModel = TrackerOverviewViewModel(
            preferences = preferences,
            trackerUseCases = trackerUseCases
        )
        searchViewModel = SearchViewModel(
            trackerUseCases = trackerUseCases,
            filterOutDigitsUseCase = FilterOutDigitsUseCase()
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
                        navGraph = TrackerOverviewNavGraph,
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
                        composable(TrackerOverviewScreenDestination) {
                            TrackerOverviewScreen(
                                navigator = buildDependencies().require(),
                                viewModel = trackerOverviewViewModel
                            )
                        }
                        composable(SearchScreenDestination) {
                            SearchScreen(
                                scaffoldState = buildDependencies().require(),
                                mealName = navArgs.mealName,
                                dayOfMonth = navArgs.dayOfMonth,
                                month = navArgs.month,
                                year = navArgs.year,
                                navigator = buildDependencies().require(),
                                viewModel = searchViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addBreakfast_appearsUnderBreakfast_nutrientsProperlyCalculated() {
        repositoryFake.searchResults = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                carbsPer100g = 50,
                proteinPer100g = 5,
                fatPer100g = 1
            )
        )
        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCabs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        composeRule
            .onNodeWithText("Add Breakfast")
            .assertDoesNotExist()

        composeRule
            .onNodeWithContentDescription("Breakfast")
            .performClick()

        composeRule
            .onNodeWithText("Add Breakfast")
            .assertIsDisplayed()
            .performClick()

        assertThat(navController.currentDestination?.route?.startsWith(SearchScreenDestination.route)).isTrue()

        composeRule
            .onNodeWithContentDescription("SearchTextField")
            .performTextInput("banana")

        composeRule
            .onNodeWithContentDescription("Search…")
            .performClick()

        composeRule
            .onNodeWithText("Carbs")
            .performClick()

        composeRule
            .onNodeWithContentDescription("Amount")
            .performTextInput(addedAmount.toString())

        composeRule
            .onNodeWithContentDescription("Track")
            .performClick()

        assertThat(navController.currentDestination?.route?.startsWith(TrackerOverviewScreenDestination.route)).isTrue()

        composeRule
            .onAllNodesWithText(expectedCalories.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedCabs.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedProtein.toString())
            .onFirst()
            .assertIsDisplayed()

        composeRule
            .onAllNodesWithText(expectedFat.toString())
            .onFirst()
            .assertIsDisplayed()
    }
}
