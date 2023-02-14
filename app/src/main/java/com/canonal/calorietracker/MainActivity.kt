package com.canonal.calorietracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.canonal.calorietracker.navigation.navigate
import com.canonal.calorietracker.ui.theme.CalorieTrackerTheme
import com.canonal.core.navigation.Route
import com.canonal.onboarding_presentation.gender.GenderScreen
import com.canonal.onboarding_presentation.welcome.WelcomeScreen
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
                    NavHost(
                        navController = navController,
                        startDestination = Route.WELCOME
                    ) {
                        composable(route = Route.WELCOME) {
                            WelcomeScreen(onNavigate = navController::navigate)
                        }
                        composable(route = Route.AGE) {

                        }
                        composable(route = Route.GENDER) {
                            GenderScreen(onNavigate = navController::navigate)
                        }
                        composable(route = Route.HEIGHT) {

                        }
                        composable(route = Route.WEIGHT) {

                        }
                        composable(route = Route.NUTRIENT_GOAL) {

                        }
                        composable(route = Route.ACTIVITY) {

                        }
                        composable(route = Route.GOAL) {

                        }
                        composable(route = Route.TRACKER_OVERVIEW) {

                        }
                        composable(route = Route.SEARCH) {

                        }
                    }
                }
            }
        }
    }
}
