package com.canonal.onboarding_domain.use_case.weight

class WeightLimitUseCase {
    operator fun invoke(weight: Float): Boolean {
        val range: ClosedFloatingPointRange<Float> = (20f.rangeTo(650f))
        return weight !in range
    }
}
