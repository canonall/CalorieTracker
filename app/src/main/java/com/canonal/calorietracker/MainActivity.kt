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
import androidx.navigation.compose.rememberNavController
import com.canonal.calorietracker.navigation.CoreFeatureNavigator
import com.canonal.calorietracker.navigation.RootNavGraph
import com.canonal.calorietracker.ui.theme.CalorieTrackerTheme
import com.canonal.core.domain.preferences.Preferences
import com.canonal.onboarding_presentation.OnboardingNavGraph
import com.canonal.tracker_presentation.TrackerOverviewNavGraph
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
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
                    ) { paddingValues ->
                        DestinationsNavHost(
                            navGraph = RootNavGraph,
                            navController = navController,
                            startRoute = if (shouldShowOnboarding) OnboardingNavGraph else TrackerOverviewNavGraph,
                            dependenciesContainerBuilder = {
                                dependency(scaffoldState)
                                dependency(
                                    CoreFeatureNavigator(
                                        currentDestination = destination,
                                        navController = navController
                                    )
                                )
                            },
                            modifier = Modifier.padding(paddingValues)
                        )
                    }
                }
            }
        }
    }
}
