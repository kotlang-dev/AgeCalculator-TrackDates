package com.synac.agecalculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [OccasionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class OccasionDatabase : RoomDatabase() {
    abstract fun occasionDao(): OccasionDao
}