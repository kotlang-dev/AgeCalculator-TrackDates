package com.synac.agecalculator.data.local

import android.content.Context
import androidx.room.Room

object DatabaseFactory {

    fun create(context: Context): OccasionDatabase {
        return Room
            .databaseBuilder(
                context = context,
                klass = OccasionDatabase::class.java,
                name = "occasion_database"
            )
            .build()
    }
}