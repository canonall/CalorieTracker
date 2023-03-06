package com.canonal.calorietracker.onboarding_tests

import android.annotation.SuppressLint
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.canonal.calorietracker.MainActivity
import com.canonal.core.R
import com.canonal.core.domain.preferences.Preferences
import com.canonal.onboarding_domain.use_case.weight.FormatWeightUseCase
import com.canonal.onboarding_domain.use_case.weight.InitialWeightUseCase
import com.canonal.onboarding_domain.use_case.weight.WeightLimitUseCase
import com.canonal.onboarding_presentation.weight.WeightScreen
import com.canonal.onboarding_presentation.weight.WeightViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class WeightScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var preferences: Preferences
    private lateinit var weightViewModel: WeightViewModel
    private lateinit var weightTextField: String

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        weightViewModel = WeightViewModel(
            preferences = preferences,
            initialWeightUseCase = InitialWeightUseCase(),
            formatWeightUseCase = FormatWeightUseCase(),
            weightLimitUseCase = WeightLimitUseCase()
        )
        weightTextField = composeRule.activity.getString(R.string.weight_text_field)
        composeRule.activity.setContent {
            val scaffoldState = rememberScaffoldState()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState
            ) {
                WeightScreen(
                    scaffoldState = scaffoldState,
                    viewModel = weightViewModel,
                    onNextClick = {}
                )
            }
        }
    }

    @Test
    fun isEmptyWeightErrorDisplayed() {
        val emptyError = composeRule.activity.getString(R.string.error_weight_cant_be_empty)

        composeRule
            .onNodeWithContentDescription(weightTextField)
            .onChildren()
            .onFirst()
            .performTextClearance()

        composeRule
            .onNodeWithText("Next")
            .performClick()

        composeRule
            .onNodeWithText(emptyError)
            .assertIsDisplayed()
    }

    @Test
    fun isInvalidWeightErrorDisplayed() {
        val emptyError = composeRule.activity.getString(R.string.error_weight_limit)
        val invalidWeight = "700"

        composeRule
            .onNodeWithContentDescription(weightTextField)
            .onChildren()
            .onFirst()
            .performTextClearance()

        composeRule
            .onNodeWithContentDescription(weightTextField)
            .onChildren()
            .onFirst()
            .performTextInput(invalidWeight)

        composeRule
            .onNodeWithText("Next")
            .performClick()

        composeRule
            .onNodeWithText(emptyError)
            .assertIsDisplayed()
    }
}
