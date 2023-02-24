package com.canonal.tracker_domain.use_case

import com.canonal.tracker_domain.model.TrackableFood
import com.canonal.tracker_domain.repository.TrackerRepository

class SearchFoodUseCase(private val trackerRepository: TrackerRepository) {
    suspend operator fun invoke(
        query: String,
        page: Int = 1,
        pageSize: Int = 40
    ): Result<List<TrackableFood>> {
        if (query.isBlank()) {
            return Result.success(emptyList())
        }
        return trackerRepository.searchFood(
            query = query.trim(),
            page = page,
            pageSize = pageSize
        )
    }
}
