package com.synac.agecalculator.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.room.RoomDatabase
import com.synac.agecalculator.data.local.OccasionDatabase
import com.synac.agecalculator.data.local.getDesktopFile
import com.synac.agecalculator.data.repository.DesktopAppUpdateRepository
import com.synac.agecalculator.data.util.Constants
import com.synac.agecalculator.domain.repository.AppUpdateRepository
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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

    singleOf(::DesktopAppUpdateRepository) bind AppUpdateRepository::class

}