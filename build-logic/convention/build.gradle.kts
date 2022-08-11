plugins {
    `kotlin-dsl`
}

group = "soup.movie.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(libs.android.pluginGradle)
    implementation(libs.kotlin.pluginGradle)
    implementation(libs.spotless.pluginGradle)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "moop.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidDynamicFeature") {
            id = "moop.android.dynamic-feature"
            implementationClass = "AndroidDynamicFeatureConventionPlugin"
        }
        register("androidLibrary") {
            id = "moop.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "moop.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidDynamicFeatureCompose") {
            id = "moop.android.dynamic-feature.compose"
            implementationClass = "AndroidDynamicFeatureComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "moop.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidTest") {
            id = "moop.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("spotless") {
            id = "moop.spotless"
            implementationClass = "SpotlessConventionPlugin"
        }
    }
}
