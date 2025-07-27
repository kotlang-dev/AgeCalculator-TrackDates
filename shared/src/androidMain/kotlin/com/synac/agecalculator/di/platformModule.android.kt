package com.synac.agecalculator.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.synac.agecalculator.data.local.OccasionDatabase
import com.synac.agecalculator.data.repository.AndroidAppUpdateRepository
import com.synac.agecalculator.data.util.Constants
import com.synac.agecalculator.domain.repository.AppUpdateRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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
            androidContext().preferencesDataStoreFile(Constants.LEGACY_DATASTORE_NAME)
        }
    }

    single<AppUpdateManager> { AppUpdateManagerFactory.create(androidContext()) }
    singleOf(::AndroidAppUpdateRepository) bind AppUpdateRepository::class

}