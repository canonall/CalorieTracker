package com.canonal.onboarding_domain.use_case.weight

class FormatWeightUseCase {
    operator fun invoke(displayedText: String, newValue: String): String {
        var newText = newValue
        if (newText.length <= 5) {
            if (newText.contains(",")) {
                newText = newText.replace(oldValue = ",", newValue = ".")
            }
            return newText
        }
        return displayedText
    }
}