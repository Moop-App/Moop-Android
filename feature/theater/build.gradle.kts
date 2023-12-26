plugins {
    id("moop.android.library")
    id("moop.android.compose")
    id("moop.android.hilt")
}

android {
    namespace = "soup.movie.feature.theater"
}

dependencies {
    implementation(project(":core:kotlin"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:logger"))
    implementation(project(":core:resources"))
    implementation(project(":data:settings:api"))
    implementation(project(":data:repository:api"))
    implementation(project(":data:model"))

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.accompanist.pagerIndicators)

    testImplementation(project(":testing"))
    androidTestImplementation(project(":testing"))
}
