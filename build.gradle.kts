@file:Suppress("DSL_SCOPE_VIOLATION")

import java.util.Properties

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dependencyGuard) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.performance) apply false
}

apply(from = "$rootDir/gradle/version.gradle")

fun propOrDef(propertyName: String, defaultValue: Any): Any {
    val properties = Properties()
    val keyFile = rootProject.file("signing/key.properties")
    if (keyFile.exists()) {
        keyFile.inputStream().use { properties.load(it) }
    }
    return properties.getProperty(propertyName) ?: defaultValue
}
