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
import com.canonal.core.domain.use_case.FilterOutDigitsUseCase
import com.canonal.onboarding_domain.use_case.height.InitialHeightUseCase
import com.canonal.onboarding_presentation.height.HeightScreen
import com.canonal.onboarding_presentation.height.HeightViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HeightScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var preferences: Preferences
    private lateinit var heightViewModel: HeightViewModel
    private lateinit var heightTextField: String

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        heightViewModel = HeightViewModel(
            preferences = preferences,
            filterOutDigitsUseCase = FilterOutDigitsUseCase(),
            initialHeightUseCase = InitialHeightUseCase()
        )
        heightTextField = composeRule.activity.getString(R.string.height_text_field)
        composeRule.activity.setContent {
            val scaffoldState = rememberScaffoldState()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState
            ) {
                HeightScreen(
                    scaffoldState = scaffoldState,
                    viewModel = heightViewModel,
                    onNextClick = {}
                )
            }
        }
    }

    @Test
    fun isEmptyHeightErrorDisplayed() {
        val emptyError = composeRule.activity.getString(R.string.error_height_cant_be_empty)

        composeRule
            .onNodeWithContentDescription(heightTextField)
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
}