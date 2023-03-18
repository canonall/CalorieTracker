package com.canonal.onboarding_domain.di

import com.canonal.onboarding_domain.use_case.age.AgeLimitUseCase
import com.canonal.onboarding_domain.use_case.height.InitialHeightUseCase
import com.canonal.onboarding_domain.use_case.nutrient_goal.ValidateNutrientsUseCase
import com.canonal.onboarding_domain.use_case.weight.InitialWeightUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object OnboardingDomainModule {

    @Provides
    @ViewModelScoped
    fun provideInitialHeightUseCase(): InitialHeightUseCase {
        return InitialHeightUseCase()
    }

    @Provides
    @ViewModelScoped
    fun provideInitialWeightUseCase(): InitialWeightUseCase {
        return InitialWeightUseCase()
    }

    @Provides
    @ViewModelScoped
    fun provideAgeLimitUseCase(): AgeLimitUseCase {
        return AgeLimitUseCase()
    }

    @Provides
    @ViewModelScoped
    fun provideValidateNutrientsUseCase(): ValidateNutrientsUseCase {
        return ValidateNutrientsUseCase()
    }
}
