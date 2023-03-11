package com.canonal.onboarding_domain.use_case.nutrient_goal

import com.canonal.core.R
import com.canonal.core.util.UiText

class ValidateNutrientsUseCase {
    operator fun invoke(
        carbsRatioText: String,
        proteinRatioText: String,
        fatRatioText: String
    ): NutrientValidationResult {
        val carbsRatio = carbsRatioText.toIntOrNull()
        val proteinRatio = proteinRatioText.toIntOrNull()
        val fatRatio = fatRatioText.toIntOrNull()

        if (carbsRatio == null || proteinRatio == null || fatRatio == null) {
            return NutrientValidationResult.Error(
                message = UiText.StringResource(resId = R.string.error_invalid_values)
            )
        }
        if (carbsRatio + proteinRatio + fatRatio != 100) {
            return NutrientValidationResult.Error(
                message = UiText.StringResource(resId = R.string.error_not_100_percent)
            )
        }
        return NutrientValidationResult.Success(
            carbsRatio = carbsRatio / 100f,
            proteinRatio = proteinRatio / 100f,
            fatRatio = fatRatio / 100f
        )
    }

    sealed class NutrientValidationResult {
        data class Success(
            val carbsRatio: Float,
            val proteinRatio: Float,
            val fatRatio: Float
        ) : NutrientValidationResult()

        data class Error(val message: UiText) : NutrientValidationResult()
    }
}
