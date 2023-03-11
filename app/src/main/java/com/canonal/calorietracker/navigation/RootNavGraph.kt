package com.canonal.calorietracker.navigation

import com.canonal.onboarding_presentation.OnboardingNavGraph
import com.canonal.tracker_presentation.TrackerOverviewNavGraph
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

object RootNavGraph : NavGraphSpec {

    override val route: String = "root"

    override val startRoute: Route = OnboardingNavGraph

    override val nestedNavGraphs: List<NavGraphSpec> = listOf(
        OnboardingNavGraph,
        TrackerOverviewNavGraph
    )

    override val destinationsByRoute: Map<String, DestinationSpec<*>> = emptyMap()
}
