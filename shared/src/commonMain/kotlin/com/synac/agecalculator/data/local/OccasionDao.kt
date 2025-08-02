package com.synac.agecalculator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OccasionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOccasion(occasion: OccasionEntity): Long

    @Query("DELETE FROM occasions WHERE id = :occasionId")
    suspend fun deleteOccasion(occasionId: Int)

    @Query("SELECT * FROM occasions ORDER BY lastModified DESC")
    fun observeOccasions(): Flow<List<OccasionEntity>>

    @Query("SELECT * FROM occasions WHERE id = :occasionId")
    suspend fun getOccasionById(occasionId: Int): OccasionEntity?

    @Query("SELECT COUNT(*) FROM occasions")
    suspend fun getOccasionCount(): Int

}