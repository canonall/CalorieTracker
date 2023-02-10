package com.canonal.calorietracker.navigation

import androidx.navigation.NavController
import com.canonal.core.util.UiEvent

fun NavController.navigate(event: UiEvent.Navigate) {
    this.navigate(event.route)
}