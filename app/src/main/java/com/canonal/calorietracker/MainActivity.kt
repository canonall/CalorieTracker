package com.canonal.calorietracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.canonal.calorietracker.ui.theme.CalorieTrackerTheme
import com.canonal.core.domain.preferences.Preferences
import com.canonal.calorietracker.navigation.Route
import com.canonal.onboarding_presentation.activity_level.ActivityLevelScreen
import com.canonal.onboarding_presentation.age.AgeScreen
import com.canonal.onboarding_presentation.gender.GenderScreen
import com.canonal.onboarding_presentation.goal_type.GoalTypeScreen
import com.canonal.onboarding_presentation.height.HeightScreen
import com.canonal.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.canonal.onboarding_presentation.weight.WeightScreen
import com.canonal.onboarding_presentation.welcome.WelcomeScreen
import com.canonal.tracker_presentation.search.SearchScreen
import com.canonal.tracker_presentation.tracker_overview.TrackerOverviewScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shouldShowOnboarding = preferences.loadShouldShowOnboarding()

        setContent {
            CalorieTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val scaffoldState = rememberScaffoldState()
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        scaffoldState = scaffoldState
                    ) { contentPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = if (shouldShowOnboarding) Route.WELCOME
                            else Route.TRACKER_OVERVIEW,
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
                                    onNextClick = {
                                        navController.navigate(Route.TRACKER_OVERVIEW)
                                    }
                                )
                            }
                            composable(route = Route.TRACKER_OVERVIEW) {
                                TrackerOverviewScreen(
                                    onNavigateToSearch = { mealName, day, month, year ->
                                        navController.navigate(
                                            route = Route.SEARCH + "/$mealName" + "/$day" + "/$month" + "/$year"
                                        )
                                    }
                                )
                            }
                            composable(
                                route = Route.SEARCH + "/{mealName}/{dayOfMonth}/{month}/{year}",
                                arguments = listOf(
                                    navArgument("mealName") {
                                        type = NavType.StringType
                                    },
                                    navArgument("dayOfMonth") {
                                        type = NavType.IntType
                                    },
                                    navArgument("month") {
                                        type = NavType.IntType
                                    },
                                    navArgument("year") {
                                        type = NavType.IntType
                                    }
                                )
                            ) {
                                val mealName = it.arguments?.getString("mealName")!!
                                val dayOfMonth = it.arguments?.getInt("dayOfMonth")!!
                                val month = it.arguments?.getInt("month")!!
                                val year = it.arguments?.getInt("year")!!
                                SearchScreen(
                                    scaffoldState = scaffoldState,
                                    mealName = mealName,
                                    dayOfMonth = dayOfMonth,
                                    month = month,
                                    year = year,
                                    onNavigateUp = {
                                        navController.navigateUp()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
