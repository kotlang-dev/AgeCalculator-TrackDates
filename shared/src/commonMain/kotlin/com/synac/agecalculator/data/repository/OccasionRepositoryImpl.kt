package com.synac.agecalculator.data.repository

import com.synac.agecalculator.data.local.OccasionDao
import com.synac.agecalculator.data.mapper.toDomain
import com.synac.agecalculator.data.mapper.toEntity
import com.synac.agecalculator.domain.model.Occasion
import com.synac.agecalculator.domain.repository.OccasionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OccasionRepositoryImpl(
    private val occasionDao: OccasionDao
) : OccasionRepository {

    override suspend fun getOccasionCount(): Int {
        return occasionDao.getOccasionCount()
    }

    override suspend fun insertOccasion(occasion: Occasion): Int {
        val id = occasionDao.insertOccasion(occasion.toEntity())
        return id.toInt()
    }

    override suspend fun deleteOccasion(occasionId: Int) {
        occasionDao.deleteOccasion(occasionId)
    }

    override fun observeOccasions(): Flow<List<Occasion>> {
        return occasionDao
            .observeOccasions()
            .map { occasionEntities ->
                occasionEntities.map { it.toDomain() }
            }
    }

    override suspend fun getOccasionById(occasionId: Int): Occasion? {
        return occasionDao.getOccasionById(occasionId)?.toDomain()
    }
}