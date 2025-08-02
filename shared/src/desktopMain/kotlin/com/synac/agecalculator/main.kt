package com.synac.agecalculator

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.synac.agecalculator.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "AgeCalculator",
        ) {
            App()
        }
    }
}