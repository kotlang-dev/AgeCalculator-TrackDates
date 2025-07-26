package com.synac.agecalculator.data.local

import androidx.room.RoomDatabaseConstructor

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object OccasionDatabaseConstructor : RoomDatabaseConstructor<OccasionDatabase> {
    override fun initialize(): OccasionDatabase
}