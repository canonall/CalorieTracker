package com.canonal.onboarding_presentation.navigation

import com.ramcosta.composedestinations.annotation.NavGraph


interface OnboardingNavigator {

    fun navigateToNextScreen()
}

@NavGraph
annotation class OnboardingNavGraph(
    val start: Boolean = false
)
