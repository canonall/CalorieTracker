package com.canonal.onboarding_presentation.height

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import com.canonal.core.R
import com.canonal.core.util.UiEvent
import com.canonal.core_ui.spacing
import com.canonal.onboarding_presentation.components.ActionButton
import com.canonal.onboarding_presentation.components.UnitTextField
import com.canonal.onboarding_presentation.navigation.OnboardingNavGraph
import com.canonal.onboarding_presentation.navigation.OnboardingNavigator
import com.ramcosta.composedestinations.annotation.Destination

@OnboardingNavGraph
@Destination
@Composable
fun HeightScreen(
    scaffoldState: ScaffoldState,
    navigator: OnboardingNavigator,
    viewModel: HeightViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Success -> navigator.navigateToNextScreen()
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = uiEvent.message.asString(context)
                    )
                }
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.whats_your_height),
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
            UnitTextField(
                value = viewModel.height,
                onValueChange = viewModel::onHeightEnter,
                onDoneClick = viewModel::onNextClick,
                unit = stringResource(id = R.string.cm),
                modifier = Modifier.semantics {
                    contentDescription = context.getString(R.string.height_text_field)
                }
            )
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = viewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}
