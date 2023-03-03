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
import com.canonal.calorietracker.navigation.navigate
import com.canonal.calorietracker.ui.theme.CalorieTrackerTheme
import com.canonal.core.navigation.Route
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                            startDestination = Route.WELCOME,
                            modifier = Modifier.padding(contentPadding)
                        ) {
                            composable(route = Route.WELCOME) {
                                WelcomeScreen(onNavigate = navController::navigate)
                            }
                            composable(route = Route.GENDER) {
                                GenderScreen(onNavigate = navController::navigate)
                            }
                            composable(route = Route.AGE) {
                                AgeScreen(
                                    scaffoldState = scaffoldState,
                                    onNavigate = navController::navigate
                                )
                            }
                            composable(route = Route.HEIGHT) {
                                HeightScreen(
                                    scaffoldState = scaffoldState,
                                    onNavigate = navController::navigate
                                )
                            }
                            composable(route = Route.WEIGHT) {
                                WeightScreen(
                                    scaffoldState = scaffoldState,
                                    onNavigate = navController::navigate
                                )
                            }
                            composable(route = Route.ACTIVITY) {
                                ActivityLevelScreen(onNavigate = navController::navigate)
                            }
                            composable(route = Route.GOAL) {
                                GoalTypeScreen(onNavigate = navController::navigate)
                            }
                            composable(route = Route.NUTRIENT_GOAL) {
                                NutrientGoalScreen(
                                    scaffoldState = scaffoldState,
                                    onNavigate = navController::navigate
                                )
                            }
                            composable(route = Route.TRACKER_OVERVIEW) {
                                TrackerOverviewScreen(onNavigate = navController::navigate)
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
