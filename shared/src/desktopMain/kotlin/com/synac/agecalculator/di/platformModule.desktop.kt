package com.synac.agecalculator.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.room.RoomDatabase
import com.synac.agecalculator.data.local.OccasionDatabase
import com.synac.agecalculator.data.local.getDesktopFile
import okio.Path.Companion.toPath
import org.koin.dsl.module

actual val platformModule = module {

    single<RoomDatabase.Builder<OccasionDatabase>> {
        val dbFile = getDesktopFile(fileName = Constants.DATABASE_NAME)
        Room.databaseBuilder<OccasionDatabase>(dbFile.absolutePath)
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath {
            getDesktopFile(Constants.DATASTORE_NAME).absolutePath.toPath()
        }
    }

}