package com.canonal.tracker_data.repository

import com.canonal.tracker_data.local.TrackerDao
import com.canonal.tracker_data.mapper.toTrackableFood
import com.canonal.tracker_data.mapper.toTrackedFood
import com.canonal.tracker_data.mapper.toTrackedFoodEntity
import com.canonal.tracker_data.remote.OpenFoodApi
import com.canonal.tracker_domain.model.TrackableFood
import com.canonal.tracker_domain.model.TrackedFood
import com.canonal.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepositoryImpl(
    private val dao: TrackerDao,
    private val api: OpenFoodApi
) : TrackerRepository {

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return try {
            val searchDto = api.searchFood(
                query = query,
                page = page,
                pageSize = pageSize
            )
            Result.success(
                searchDto.products
                    .filter { product ->
                        // if not in range, then product is not suitable as result
                        // because it is not suitable for the app in terms of nutriments calculation
                        // only show products that fulfill the condition
                        // calories = (carbs * 4) + (protein * 4) + (fat * 9)
                        val calculatedCalories =
                            product.nutriments.carbonhydrates100g * 4f +
                                    product.nutriments.proteins100g * 4f +
                                    product.nutriments.fat100g * 9f
                        val lowerBound = calculatedCalories * 0.99f
                        val upperBound = calculatedCalories * 1.01f
                        product.nutriments.energyKcal100g in (lowerBound..upperBound)
                    }
                    .mapNotNull { product ->
                        product.toTrackableFood()
                    }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(trackedFood: TrackedFood) {
        dao.insertTrackedFood(trackedFoodEntity = trackedFood.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(trackedFood: TrackedFood) {
        dao.deleteTrackedFood(trackedFoodEntity = trackedFood.toTrackedFoodEntity())
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map { entity ->
                entity.toTrackedFood()
            }
        }
    }
}
