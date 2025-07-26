package com.synac.agecalculator.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [OccasionEntity::class],
    version = 1
)
@ConstructedBy(OccasionDatabaseConstructor::class)
abstract class OccasionDatabase : RoomDatabase() {
    abstract val occasionDao: OccasionDao
}