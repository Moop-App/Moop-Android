plugins {
    id("moop.android.library")
    id("moop.android.compose")
    id("moop.android.hilt")
}

android {
    namespace = "soup.movie.feature.home"
}

dependencies {
    implementation(project(":core:analytics:api"))
    implementation(project(":core:kotlin"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:imageloading:api"))
    implementation(project(":core:logger"))
    implementation(project(":core:resources"))
    implementation(project(":data:settings:api"))
    implementation(project(":data:repository:api"))
    implementation(project(":data:model"))
    implementation(project(":domain"))
    implementation(project(":feature:theatermap"))
    implementation(project(":feature:settings"))

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.compose.animation.graphics)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.accompanist.pagerIndicators)

    testImplementation(projects.testing)
    androidTestImplementation(projects.testing)
}
