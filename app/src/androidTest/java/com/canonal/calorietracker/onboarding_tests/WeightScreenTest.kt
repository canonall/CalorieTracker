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
import com.canonal.onboarding_domain.use_case.weight.InitialWeightUseCase
import com.canonal.onboarding_presentation.navigation.OnboardingNavigator
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
    private lateinit var weightText: String
    private val initialWeight = "80"
    private val minWeight = "20"
    private val maxWeight = "250"

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        weightViewModel = WeightViewModel(
            preferences = preferences,
            initialWeightUseCase = InitialWeightUseCase()
        )
        weightText = composeRule.activity.getString(R.string.weight_text)
        composeRule.activity.setContent {
            val scaffoldState = rememberScaffoldState()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState
            ) {
                WeightScreen(
                    viewModel = weightViewModel,
                    navigator = object : OnboardingNavigator {
                        override fun navigateToNextScreen() {}
                    }
                )
            }
        }
    }

    @Test
    fun isScaleUpdated() {
        composeRule.onNodeWithText(initialWeight).assertIsDisplayed()
        composeRule
            .onNodeWithContentDescription("scale")
            .performTouchInput {
                // updates scale from 80 to 45
                swipeRight()
            }
        composeRule.onNodeWithText("45").assertIsDisplayed()
    }

    @Test
    fun isScaleMinLimitApplied() {
        composeRule.onNodeWithText(initialWeight).assertIsDisplayed()
        composeRule
            .onNodeWithContentDescription("scale")
            .performTouchInput {
                // swipe until reaching minValue
                swipeRight(startX = 0f, endX = 3000f)
            }
        composeRule.onNodeWithText(minWeight).assertIsDisplayed()
    }

    @Test
    fun isScaleMaxLimitApplied() {
        composeRule.onNodeWithText(initialWeight).assertIsDisplayed()
        composeRule
            .onNodeWithContentDescription("scale")
            .performTouchInput {
                // swipe until reaching maxValue
                swipeLeft(startX = 0f, endX = -3000f)
                swipeLeft(startX = 0f, endX = -3000f)
                swipeLeft(startX = 0f, endX = -3000f)
                swipeLeft(startX = 0f, endX = -3000f)
                swipeLeft(startX = 0f, endX = -100f)
            }
        composeRule.onNodeWithText(maxWeight).assertIsDisplayed()
    }
}
