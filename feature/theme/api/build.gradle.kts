plugins {
    id("moop.android.library")
    id("moop.android.compose")
    id("moop.android.hilt")
}

android {
    namespace = "soup.movie.feature.theme"
}

dependencies {
    implementation(project(":core:kotlin"))
    implementation(project(":core:resources"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core)
    implementation(libs.compose.foundation)
}
