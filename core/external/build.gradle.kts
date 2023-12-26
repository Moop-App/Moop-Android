plugins {
    id("moop.android.library")
}

android {
    namespace = "soup.movie.core.external"
}

dependencies {
    implementation(project(":core:logger"))

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.browser)
}
