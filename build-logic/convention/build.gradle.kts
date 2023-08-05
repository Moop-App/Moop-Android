plugins {
    `kotlin-dsl`
}

group = "soup.movie.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.android.pluginGradle)
    implementation(libs.kotlin.pluginGradle)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "moop.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "moop.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidTest") {
            id = "moop.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("androidHilt") {
            id = "moop.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidCompose") {
            id = "moop.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidBuildConfig") {
            id = "moop.android.buildconfig"
            implementationClass = "AndroidBuildConfigConventionPlugin"
        }
    }
}
