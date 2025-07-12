package com.synac.agecalculator.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.work.WorkManager
import com.synac.agecalculator.data.local.DatabaseFactory
import com.synac.agecalculator.data.local.OccasionDao
import com.synac.agecalculator.data.local.OccasionDatabase
import com.synac.agecalculator.data.reminder.ReminderSchedulerImpl
import com.synac.agecalculator.data.repository.OccasionRepositoryImpl
import com.synac.agecalculator.data.repository.PreferenceRepositoryImpl
import com.synac.agecalculator.domain.repository.OccasionRepository
import com.synac.agecalculator.domain.repository.PreferenceRepository
import com.synac.agecalculator.domain.repository.ReminderScheduler
import com.synac.agecalculator.presentation.MainViewModel
import com.synac.agecalculator.presentation.calculator.CalculatorViewModel
import com.synac.agecalculator.presentation.dashboard.DashboardViewModel
import com.synac.agecalculator.presentation.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single<OccasionDatabase> { DatabaseFactory.create(get()) }
    single<OccasionDao> { get<OccasionDatabase>().occasionDao }
    single<WorkManager> { WorkManager.getInstance(get()) }

    singleOf(::OccasionRepositoryImpl) bind OccasionRepository::class
    singleOf(::ReminderSchedulerImpl) bind ReminderScheduler::class

    //DataStore
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile(name = "kinex_settings")
        }
    }
    singleOf(::PreferenceRepositoryImpl) bind PreferenceRepository::class

    viewModelOf(::CalculatorViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::MainViewModel)
}