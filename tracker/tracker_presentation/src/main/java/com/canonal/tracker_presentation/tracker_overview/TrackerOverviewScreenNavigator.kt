package com.canonal.tracker_presentation.tracker_overview

import com.ramcosta.composedestinations.annotation.NavGraph

interface TrackerOverviewScreenNavigator {

    fun navigateToSearch(mealName: String, dayOfMonth: Int, month: Int, year: Int)
}

@NavGraph
annotation class TrackerOverviewNavGraph(
    val start: Boolean = false
)
