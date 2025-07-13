package com.synac.agecalculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [OccasionEntity::class],
    version = 1
)
abstract class OccasionDatabase : RoomDatabase() {
    abstract val occasionDao: OccasionDao
}