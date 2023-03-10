package com.canonal.onboarding_presentation.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.canonal.core.R
import com.canonal.core_ui.spacing
import com.canonal.onboarding_presentation.components.ActionButton
import com.canonal.onboarding_presentation.navigation.OnboardingNavGraph
import com.canonal.onboarding_presentation.navigation.OnboardingNavigator
import com.ramcosta.composedestinations.annotation.Destination

@OnboardingNavGraph(start = true)
@Destination
@Composable
fun WelcomeScreen(
    navigator: OnboardingNavigator
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.spaceMedium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = { navigator.navigateToNextScreen() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
