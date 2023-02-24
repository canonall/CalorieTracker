package com.canonal.tracker_domain.use_case

/**
 * Wrapper class to pass all use cases into viewmodel
 */
data class TrackerUseCases(
    val trackFoodUseCase: TrackFoodUseCase,
    val searchFoodUseCase: SearchFoodUseCase,
    val getFoodsForDateUseCase: GetFoodsForDateUseCase,
    val deleteTrackedFoodUseCase: DeleteTrackedFoodUseCase,
    val calculateMealNutrientsUseCase: CalculateMealNutrientsUseCase
)
