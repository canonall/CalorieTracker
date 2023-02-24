package com.canonal.tracker_domain.use_case

import com.canonal.tracker_domain.model.TrackedFood
import com.canonal.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetFoodsForDateUseCase(private val trackerRepository: TrackerRepository) {
    operator fun invoke(date: LocalDate): Flow<List<TrackedFood>> {
        return trackerRepository.getFoodsForDate(localDate = date)
    }
}
