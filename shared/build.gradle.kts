import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.androidx.lifecycle.viewmodel.kmp)
            implementation(libs.androidx.lifecycle.viewmodel.compose.kmp)
            implementation(libs.androidx.lifecycle.runtime.compose.kmp)
            implementation("org.jetbrains.compose.ui:ui-backhandler:1.8.0-alpha03")

            //Compose
            implementation(libs.navigation.compose.kmp)
            implementation(libs.bundles.adaptive.layout.kmp)

            //Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            //Datastore
            implementation(libs.androidx.datastore.preferences)

            //Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            //Kotlinx
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.app.update.ktx)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.synac.agecalculator.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose.desktop {
    application {
        mainClass = "com.synac.agecalculator.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.synac.agecalculator"
            packageVersion = libs.versions.app.versionName.get()
            description = "An app to track important dates and calculate ages."
            copyright = "© 2025 Mohammad Arif. All rights reserved."
            vendor = "Mohammad Arif"

            javaHome = "C:/Users/Mohammad Arif/.gradle/jdks/eclipse_adoptium-17-amd64-windows.2"

            macOS {
                iconFile.set(project.file("app_icon.icns"))
            }
            windows {
                iconFile.set(project.file("app_icon.ico"))
            }
            linux {
                iconFile.set(project.file("app_icon.png"))
            }
        }
    }
}


dependencies {
    debugImplementation(compose.uiTooling)
    ksp(libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}