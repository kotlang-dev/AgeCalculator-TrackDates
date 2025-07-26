package com.synac.agecalculator.presentation

import android.app.Application
import com.synac.agecalculator.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AgeCalculatorApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AgeCalculatorApp)
            modules(appModule)
        }
    }
}