package com.canonal.onboarding_domain.use_case.age

class AgeLimitUseCase {
    operator fun invoke(age: Int): Boolean = age !in 8..100
}