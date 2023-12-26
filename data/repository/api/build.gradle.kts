plugins {
    id("moop.android.library")
}

android {
    namespace = "soup.movie.data.repository"
}

dependencies {
    implementation(project(":data:model"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.coroutines.core)
}
