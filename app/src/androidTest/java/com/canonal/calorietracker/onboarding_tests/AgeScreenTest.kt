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
import com.canonal.onboarding_domain.use_case.age.AgeLimitUseCase
import com.canonal.onboarding_presentation.age.AgeScreen
import com.canonal.onboarding_presentation.age.AgeViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AgeScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var preferences: Preferences
    private lateinit var ageViewModel: AgeViewModel
    private lateinit var ageTextField: String

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        ageViewModel = AgeViewModel(
            preferences = preferences,
            filterOutDigitsUseCase = FilterOutDigitsUseCase(),
            ageLimitUseCase = AgeLimitUseCase()
        )
        ageTextField = composeRule.activity.getString(R.string.age_text_field)
        composeRule.activity.setContent {
            val scaffoldState = rememberScaffoldState()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState
            ) {
                AgeScreen(
                    scaffoldState = scaffoldState,
                    viewModel = ageViewModel,
                    onNextClick = {}
                )
            }
        }
    }

    @Test
    fun isdEmptyAgeErrorDisplayed() {
        val emptyAgeError = composeRule.activity.getString(R.string.error_age_cant_be_empty)

        composeRule
            .onNodeWithContentDescription(ageTextField)
            .onChildren()
            .onFirst()
            .performTextClearance()

        composeRule
            .onNodeWithText("Next")
            .performClick()

        composeRule
            .onNodeWithText(emptyAgeError)
            .assertIsDisplayed()
    }

    @Test
    fun isInvalidAgeErrorDisplayed() {
        val ageLimitError = composeRule.activity.getString(R.string.error_age_limit)
        val invalidAge = "110"

        composeRule
            .onNodeWithContentDescription(ageTextField)
            .onChildren()
            .onFirst()
            .performTextClearance()

        composeRule
            .onNodeWithContentDescription(ageTextField)
            .onChildren()
            .onFirst()
            .performTextInput(invalidAge)

        composeRule
            .onNodeWithText("Next")
            .performClick()

        composeRule
            .onNodeWithText(ageLimitError)
            .assertIsDisplayed()
    }
}