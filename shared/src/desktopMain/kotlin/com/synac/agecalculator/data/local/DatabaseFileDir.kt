package com.synac.agecalculator.data.local

import java.io.File

fun getDatabaseFile(): File {
    val userHome = System.getProperty("user.home")

    val appDataDir = when {
        System.getProperty("os.name").startsWith("Windows") -> {
            File(System.getenv("APPDATA"), "AgeCalculator")
        }
        System.getProperty("os.name") == "Mac OS X" -> {
            File(userHome, "Library/Application Support/AgeCalculator")
        }
        else -> {
            File(userHome, ".AgeCalculator")
        }
    }

    if (!appDataDir.exists()) {
        appDataDir.mkdirs()
    }

    return File(appDataDir, "occasions.db")
}