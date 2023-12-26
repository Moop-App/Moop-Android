plugins {
    id("moop.android.library")
}

android {
    namespace = "soup.movie.feature.navigator"
}

dependencies {
    implementation(project(":core:logger"))

    implementation(libs.kotlin.stdlib)
}
