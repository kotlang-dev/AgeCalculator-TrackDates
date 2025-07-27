package com.synac.agecalculator

import android.app.Application
import com.synac.agecalculator.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AgeCalculatorApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@AgeCalculatorApp)
        }
    }
}