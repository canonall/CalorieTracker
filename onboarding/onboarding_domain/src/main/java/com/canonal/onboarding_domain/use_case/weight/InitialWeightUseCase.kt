package com.canonal.onboarding_domain.use_case.weight

import com.canonal.core.domain.model.Gender

class InitialWeightUseCase {
    operator fun invoke(gender: Gender): String {
        return when (gender) {
            Gender.Female -> "60.0"
            Gender.Male -> "80.0"
        }
    }
}
