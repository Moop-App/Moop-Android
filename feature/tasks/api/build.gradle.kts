plugins {
    id("moop.android.library")
}

android {
    namespace = "soup.movie.feature.tasks"
}

dependencies {
    implementation(project(":data:model"))

    implementation(libs.kotlin.stdlib)
}
