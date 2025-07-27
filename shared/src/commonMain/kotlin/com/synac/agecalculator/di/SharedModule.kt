package com.synac.agecalculator.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.synac.agecalculator.data.local.OccasionDao
import com.synac.agecalculator.data.local.OccasionDatabase
import com.synac.agecalculator.data.repository.OccasionRepositoryImpl
import com.synac.agecalculator.data.repository.PreferenceRepositoryImpl
import com.synac.agecalculator.domain.repository.OccasionRepository
import com.synac.agecalculator.domain.repository.PreferenceRepository
import com.synac.agecalculator.presentation.calculator.CalculatorViewModel
import com.synac.agecalculator.presentation.dashboard.DashboardViewModel
import com.synac.agecalculator.presentation.main.MainViewModel
import com.synac.agecalculator.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {

    single<OccasionDatabase> {
        get<RoomDatabase.Builder<OccasionDatabase>>()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single<OccasionDao> { get<OccasionDatabase>().occasionDao }

    singleOf(::OccasionRepositoryImpl).bind<OccasionRepository>()

    singleOf(::PreferenceRepositoryImpl) bind PreferenceRepository::class

    viewModelOf(::CalculatorViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::MainViewModel)

}