package com.canonal.onboarding_presentation.weight

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.canonal.core.R
import com.canonal.core.util.UiEvent
import com.canonal.core_ui.spacing
import com.canonal.onboarding_presentation.components.ActionButton
import com.canonal.onboarding_presentation.navigation.OnboardingNavGraph
import com.canonal.onboarding_presentation.navigation.OnboardingNavigator
import com.canonal.onboarding_presentation.weight.scale.Scale
import com.ramcosta.composedestinations.annotation.Destination

@OnboardingNavGraph
@Destination
@Composable
fun WeightScreen(
    navigator: OnboardingNavigator,
    viewModel: WeightViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Success -> navigator.navigateToNextScreen()
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
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = MaterialTheme.spacing.spaceExtraLarge),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.whats_your_weight),
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viewModel.weight,
                    fontSize = 70.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier
                        .alignBy(LastBaseline)
                        .semantics {
                            contentDescription = context.getString(R.string.weight_text)
                        }
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceSmall))
                Text(
                    text = stringResource(id = R.string.kg),
                    modifier = Modifier.alignBy(LastBaseline)
                )
            }
        }
        Scale(
            initialWeight = viewModel.getInitialWeight().toInt(),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.BottomCenter)
                .semantics {
                    contentDescription = "scale"
                },
            onWeightChange = { weight ->
                viewModel.onWeightEnter(weight.toString())
            }
        )
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = viewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}
