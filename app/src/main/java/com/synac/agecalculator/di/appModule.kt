package com.synac.agecalculator.di

import com.synac.agecalculator.data.local.DatabaseFactory
import com.synac.agecalculator.data.local.OccasionDao
import com.synac.agecalculator.data.local.OccasionDatabase
import com.synac.agecalculator.data.repository.OccasionRepositoryImpl
import com.synac.agecalculator.domain.repository.OccasionRepository
import com.synac.agecalculator.presentation.calculator.CalculatorViewModel
import com.synac.agecalculator.presentation.dashboard.DashboardViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    single<OccasionDatabase> { DatabaseFactory.create(get()) }
    single<OccasionDao> { get<OccasionDatabase>().occasionDao() }

    singleOf(::OccasionRepositoryImpl).bind<OccasionRepository>()

    viewModelOf(::CalculatorViewModel)
    viewModelOf(::DashboardViewModel)
}