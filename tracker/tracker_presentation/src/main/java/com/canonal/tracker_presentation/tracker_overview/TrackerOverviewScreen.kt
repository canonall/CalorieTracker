package com.canonal.tracker_presentation.tracker_overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.canonal.core.R
import com.canonal.core_ui.spacing
import com.canonal.tracker_presentation.tracker_overview.components.*
import com.ramcosta.composedestinations.annotation.Destination

@TrackerOverviewNavGraph(start = true)
@Destination
@Composable
fun TrackerOverviewScreen(
    navigator: TrackerOverviewScreenNavigator,
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = MaterialTheme.spacing.spaceMedium)
    ) {
        item {
            NutrientsHeader(state = state)
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
            DaySelector(
                date = state.date,
                onPreviousDayClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnPreviousDayClick)
                },
                onNextDayClick = { viewModel.onEvent(TrackerOverviewEvent.OnNextDayClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
        }
        items(items = state.meals) { meal ->
            ExpandableMeal(
                meal = meal,
                onToggleClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnToggleMealClick(meal = meal))
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.spaceSmall)
                    ) {
                        val foodsPerMeal = state.trackedFoodList.filter { trackedFood ->
                            trackedFood.mealType == meal.mealType
                        }
                        foodsPerMeal.forEach { trackedFood ->
                            TrackedFoodItem(
                                trackedFood = trackedFood,
                                onDeleteClick = {
                                    viewModel.onEvent(
                                        TrackerOverviewEvent.OnDeleteTrackedFoodClick(
                                            trackedFood = trackedFood
                                        )
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
                        }
                        AddButton(
                            text = stringResource(
                                id = R.string.add_meal,
                                meal.name.asString(context)
                            ),
                            onClick = {
                                navigator.navigateToSearch(
                                    meal.name.asString(context),
                                    state.date.dayOfMonth,
                                    state.date.monthValue,
                                    state.date.year
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
