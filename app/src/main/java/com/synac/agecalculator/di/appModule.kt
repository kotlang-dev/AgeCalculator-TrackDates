package com.synac.agecalculator.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import androidx.work.WorkManager
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.synac.agecalculator.data.local.OccasionDao
import com.synac.agecalculator.data.local.OccasionDatabase
import com.synac.agecalculator.data.reminder.ReminderSchedulerImpl
import com.synac.agecalculator.data.repository.AppUpdateRepositoryImpl
import com.synac.agecalculator.data.repository.OccasionRepositoryImpl
import com.synac.agecalculator.data.repository.PreferenceRepositoryImpl
import com.synac.agecalculator.domain.repository.AppUpdateRepository
import com.synac.agecalculator.domain.repository.OccasionRepository
import com.synac.agecalculator.domain.repository.PreferenceRepository
import com.synac.agecalculator.domain.repository.ReminderScheduler
import com.synac.agecalculator.presentation.main.MainViewModel
import com.synac.agecalculator.presentation.calculator.CalculatorViewModel
import com.synac.agecalculator.presentation.dashboard.DashboardViewModel
import com.synac.agecalculator.presentation.settings.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    //Room Db
    single<OccasionDatabase> {
        Room
            .databaseBuilder(
                context = androidApplication(),
                klass = OccasionDatabase::class.java,
                name = "occasion_database"
            )
            .build()
    }
    single<OccasionDao> { get<OccasionDatabase>().occasionDao }

    singleOf(::OccasionRepositoryImpl).bind<OccasionRepository>()

    //DataStore
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile(name = "kinex_settings")
        }
    }
    singleOf(::PreferenceRepositoryImpl) bind PreferenceRepository::class

    //Reminder
    single<WorkManager> { WorkManager.getInstance(get()) }
    singleOf(::ReminderSchedulerImpl) bind ReminderScheduler::class

    //App Update
    single<AppUpdateManager> { AppUpdateManagerFactory.create(androidContext()) }
    singleOf(::AppUpdateRepositoryImpl) bind AppUpdateRepository::class

    viewModelOf(::CalculatorViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::MainViewModel)
}