package com.canonal.calorietracker.repository

import com.canonal.tracker_domain.model.TrackableFood
import com.canonal.tracker_domain.model.TrackedFood
import com.canonal.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDate
import kotlin.random.Random

class TrackerRepositoryFake : TrackerRepository {

    private val getFoodsForDateFlow = MutableSharedFlow<List<TrackedFood>>(replay = 1)
    private val trackedFoodList = mutableListOf<TrackedFood>()
    var searchResults = listOf<TrackableFood>()
    var shouldReturnError = false

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return if (shouldReturnError) {
            Result.failure(Throwable())
        } else {
            Result.success(searchResults)
        }
    }

    override suspend fun insertTrackedFood(trackedFood: TrackedFood) {
        trackedFoodList.add(trackedFood.copy(id = Random.nextInt()))
        getFoodsForDateFlow.emit(trackedFoodList)
    }

    override suspend fun deleteTrackedFood(trackedFood: TrackedFood) {
        trackedFoodList.remove(trackedFood)
        getFoodsForDateFlow.emit(trackedFoodList)
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return getFoodsForDateFlow
    }
}
