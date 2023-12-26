plugins {
    id("moop.android.library")
    id("moop.android.hilt")
}

android {
    namespace = "soup.movie.core.appupdate.impl"
}

dependencies {
    implementation(project(":core:kotlin"))
    implementation(project(":core:appupdate:api"))
    implementation(project(":core:logger"))
    implementation(libs.kotlin.stdlib)

    implementation(libs.google.play.appupdate)
}
