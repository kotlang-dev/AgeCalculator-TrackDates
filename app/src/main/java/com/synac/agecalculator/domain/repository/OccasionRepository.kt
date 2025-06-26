package com.synac.agecalculator.domain.repository

import com.synac.agecalculator.domain.model.Occasion
import kotlinx.coroutines.flow.Flow

interface OccasionRepository {
    suspend fun insertOccasion(occasion: Occasion): Int
    suspend fun deleteOccasion(occasionId: Int)
    fun getAllOccasions(): Flow<List<Occasion>>
    suspend fun getOccasionById(occasionId: Int): Occasion?
}