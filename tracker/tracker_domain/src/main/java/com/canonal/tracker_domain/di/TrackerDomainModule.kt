package com.canonal.tracker_domain.di

import com.canonal.core.domain.preferences.Preferences
import com.canonal.tracker_domain.repository.TrackerRepository
import com.canonal.tracker_domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @Provides
    @ViewModelScoped
    fun provideTrackerUseCases(
        trackerRepository: TrackerRepository,
        preferences: Preferences
    ): TrackerUseCases {
        return TrackerUseCases(
            trackFoodUseCase = TrackFoodUseCase(trackerRepository),
            searchFoodUseCase = SearchFoodUseCase(trackerRepository),
            getFoodsForDateUseCase = GetFoodsForDateUseCase(trackerRepository),
            deleteTrackedFoodUseCase = DeleteTrackedFoodUseCase(trackerRepository),
            calculateMealNutrientsUseCase = CalculateMealNutrientsUseCase(preferences)
        )
    }
}
