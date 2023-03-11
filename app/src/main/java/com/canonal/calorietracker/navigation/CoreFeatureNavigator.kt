package com.canonal.calorietracker.navigation

import androidx.navigation.NavController

import com.canonal.onboarding_presentation.destinations.*
import com.canonal.onboarding_presentation.navigation.OnboardingNavigator
import com.canonal.tracker_presentation.destinations.SearchScreenDestination
import com.canonal.tracker_presentation.destinations.TrackerOverviewScreenDestination
import com.canonal.tracker_presentation.search.SearchScreenNavigator
import com.canonal.tracker_presentation.tracker_overview.TrackerOverviewScreenNavigator
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DestinationSpec

class CoreFeatureNavigator(
    private val currentDestination: DestinationSpec<*>,
    private val navController: NavController
) : SearchScreenNavigator,
    TrackerOverviewScreenNavigator,
    OnboardingNavigator {

    override fun navigateToNextScreen() {
        currentDestination as? OnboardingDestination
            ?: throw RuntimeException("Trying to use Onboarding navigator from a non onboarding screen")

        val nextDirection = when (currentDestination) {
            WelcomeScreenDestination -> GenderScreenDestination
            GenderScreenDestination -> AgeScreenDestination
            AgeScreenDestination -> HeightScreenDestination
            HeightScreenDestination -> WeightScreenDestination
            WeightScreenDestination -> ActivityLevelScreenDestination
            ActivityLevelScreenDestination -> GoalTypeScreenDestination
            GoalTypeScreenDestination -> NutrientGoalScreenDestination
            NutrientGoalScreenDestination -> TrackerOverviewScreenDestination
        }

        navController.navigate(nextDirection)
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

    override fun navigateToSearch(mealName: String, dayOfMonth: Int, month: Int, year: Int) {
        navController.navigate(
            SearchScreenDestination(
                mealName = mealName,
                dayOfMonth = dayOfMonth,
                month = month,
                year = year
            )
        )
    }
}
