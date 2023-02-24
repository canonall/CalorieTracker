package com.canonal.tracker_domain.use_case

import com.canonal.tracker_domain.model.TrackedFood
import com.canonal.tracker_domain.repository.TrackerRepository

class DeleteTrackedFoodUseCase(private val trackerRepository: TrackerRepository) {
    suspend operator fun invoke(trackedFood: TrackedFood) {
        trackerRepository.deleteTrackedFood(trackedFood)
    }
}
