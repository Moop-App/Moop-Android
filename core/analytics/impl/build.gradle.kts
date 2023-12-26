plugins {
    id("moop.android.library")
    id("moop.android.hilt")
}

android {
    namespace = "soup.movie.data.tracking.impl"
}

dependencies {
    implementation(project(":core:kotlin"))
    implementation(project(":core:analytics:api"))
    implementation(libs.kotlin.stdlib)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}
