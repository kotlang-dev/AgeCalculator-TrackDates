package com.synac.agecalculator.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.synac.agecalculator.data.local.OccasionDatabase
import com.synac.agecalculator.data.local.getDatabaseFile
import org.koin.dsl.module

actual val platformModule = module {

    single<RoomDatabase.Builder<OccasionDatabase>> {
        val dbFile = getDatabaseFile()
        Room.databaseBuilder<OccasionDatabase>(dbFile.absolutePath)
    }

}