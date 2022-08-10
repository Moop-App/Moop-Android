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
        register("spotless") {
            id = "moop.spotless"
            implementationClass = "SpotlessConventionPlugin"
        }
    }
}
