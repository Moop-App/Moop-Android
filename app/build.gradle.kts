@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

import java.util.Properties

plugins {
    id("moop.android.application")
    id("moop.android.compose")
    id("moop.android.hilt")
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)
    alias(libs.plugins.dependencyGuard)
}
if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
}

val useReleaseKeystore = rootProject.file("signing/app-release.jks").exists()

android {
    namespace = "soup.movie"
    defaultConfig {
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("signing/app-debug.jks")
            storePassword = "android"
            keyAlias = "android"
            keyPassword = "android"
        }

        maybeCreate("release").apply {
            if (useReleaseKeystore) {
                val properties = Properties()
                val keyFile = rootProject.file("signing/key.properties")
                if (keyFile.exists()) {
                    keyFile.inputStream().use { properties.load(it) }
                }

                storeFile = rootProject.file("signing/app-release.jks")
                storePassword = properties.getProperty("STORE_PASSWORD", "")
                keyAlias = properties.getProperty("KEY_ALIAS", "")
                keyPassword = properties.getProperty("KEY_PASSWORD", "")
            }
        }
    }
    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
            manifestPlaceholders += mapOf("crashlytics_enabled" to false)
        }
        create("benchmark") {
            signingConfig = if (useReleaseKeystore) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
            matchingFallbacks += listOf("release")
            isDebuggable = false
            manifestPlaceholders += mapOf("crashlytics_enabled" to true)
        }
        getByName("release") {
            signingConfig = if (useReleaseKeystore) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders += mapOf("crashlytics_enabled" to true)
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true

    }
    lint {
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(project(":core:ads"))
    implementation(project(":core:analytics:api"))
    runtimeOnly(project(":core:analytics:impl"))
    implementation(project(":core:appupdate:api"))
    implementation(project(":core:kotlin"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:logger"))
    implementation(project(":data:repository:api"))
    implementation(project(":data:model"))
    implementation(project(":feature:home"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:search"))
    implementation(project(":feature:theater"))
    implementation(project(":feature:theatermap"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:theme:api"))
    runtimeOnly(project(":feature:theme:impl"))
    implementation(project(":feature:navigator:api"))
    runtimeOnly(project(":feature:navigator:impl"))
    implementation(project(":feature:notification:api"))
    runtimeOnly(project(":feature:notification:impl"))
    implementation(project(":feature:tasks:api"))
    runtimeOnly(project(":feature:tasks:impl"))
    implementation(project(":feature:deeplink"))
    runtimeOnly(project(":data:network:impl"))
    runtimeOnly(project(":data:database:impl"))
    runtimeOnly(project(":data:repository:impl"))
    runtimeOnly(project(":data:settings:impl"))
    runtimeOnly(project(":core:appupdate:impl"))
    runtimeOnly(project(":core:imageloading:impl"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.startup)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.compiler)

    implementation(libs.androidx.navigation.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.cloudmessaging)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.dynamiclinks)
    implementation(libs.firebase.performance)
    implementation(libs.firebase.remoteconfig)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.accompanist.drawablepainter)

    implementation(libs.androidx.profileinstaller)

    implementation(libs.kakao.share)

    testImplementation(project(":testing"))
    androidTestImplementation(project(":testing"))
}

dependencyGuard {
    // All dependencies included in Production Release APK
    configuration("releaseRuntimeClasspath")
}

tasks.register("applyDependencyBaseline") {
    dependsOn("dependencyGuardBaseline")
}
