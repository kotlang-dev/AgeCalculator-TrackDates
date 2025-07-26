package com.synac.agecalculator.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
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
            name = Constants.DATABASE_NAME
        )
    }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile(Constants.DATASTORE_NAME)
        }
    }

}