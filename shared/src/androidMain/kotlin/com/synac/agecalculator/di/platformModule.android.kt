package com.synac.agecalculator.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.synac.agecalculator.data.local.OccasionDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {

    single<RoomDatabase.Builder<OccasionDatabase>> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = OccasionDatabase::class.java,
            name = "occasions.db"
        )
    }

}