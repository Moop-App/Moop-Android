plugins {
    id("moop.android.library")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "soup.movie.data.network"
}

dependencies {
    implementation(project(":data:model"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.serialization)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.androidx.core)
}
