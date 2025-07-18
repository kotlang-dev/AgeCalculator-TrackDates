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
                if (occasionEntities.isNotEmpty()) {
                    occasionEntities.map { it.toDomain() }
                } else {
                    val default = Occasion(
                        id = null,
                        title = "Birthday",
                        dateMillis = 0L,
                        emoji = "🎂",
                        isReminderEnabled = false
                    )
                    val id = occasionDao.insertOccasion(default.toEntity())
                    listOf(default.copy(id = id.toInt()))
                }
            }
    }

    override suspend fun getOccasionById(occasionId: Int): Occasion? {
        return occasionDao.getOccasionById(occasionId)?.toDomain()
    }
}