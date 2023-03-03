package com.canonal.tracker_presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.canonal.core.R
import com.canonal.core.util.UiEvent
import com.canonal.core_ui.spacing
import com.canonal.tracker_domain.model.MealType
import com.canonal.tracker_presentation.search.components.SearchTextField
import com.canonal.tracker_presentation.search.components.TrackableFoodItem
import java.time.LocalDate

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    scaffoldState: ScaffoldState,
    mealName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onNavigateUp: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val state = viewModel.state

    LaunchedEffect(key1 = keyboardController) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.NavigateUp -> onNavigateUp()
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message.asString(context)
                    )
                    keyboardController?.hide()
                }
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.spaceMedium)
    ) {
        Text(
            text = stringResource(id = R.string.add_meal, mealName),
            style = MaterialTheme.typography.h5
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
        SearchTextField(
            text = state.query,
            onValueChange = { text ->
                viewModel.onEvent(SearchEvent.OnQueryChange(query = text))
            },
            onSearch = { viewModel.onEvent(SearchEvent.OnSearch) },
            onFocusChanged = { focusState ->
                viewModel.onEvent(SearchEvent.OnSearchFocusChange(isFocused = focusState.isFocused))
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.trackableFoodItemList) { trackableFoodItem ->
                TrackableFoodItem(
                    trackableFoodItem = trackableFoodItem,
                    onClick = {
                        viewModel.onEvent(
                            SearchEvent.OnToggleTrackableFood(
                                trackableFood = trackableFoodItem.trackableFood
                            )
                        )
                    },
                    onAmountChange = { text ->
                        viewModel.onEvent(
                            SearchEvent.OnAmountForTrackableFoodChange(
                                trackableFood = trackableFoodItem.trackableFood,
                                amount = text
                            )
                        )
                    },
                    onTrack = {
                        viewModel.onEvent(
                            SearchEvent.OnTrackFoodClick(
                                trackableFood = trackableFoodItem.trackableFood,
                                mealType = MealType.fromString(trackableFoodItem.trackableFood.name),
                                date = LocalDate.of(year, month, dayOfMonth)
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isSearching -> CircularProgressIndicator()
            state.trackableFoodItemList.isEmpty() -> {
                Text(
                    text = stringResource(id = R.string.no_results),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
