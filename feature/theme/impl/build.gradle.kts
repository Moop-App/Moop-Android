plugins {
    id("moop.android.library")
    id("moop.android.compose")
    id("moop.android.hilt")
}

android {
    namespace = "soup.movie.feature.theme.impl"
}

dependencies {
    implementation(project(":core:kotlin"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:resources"))
    implementation(project(":data:settings:api"))
    implementation(project(":data:model"))
    implementation(project(":feature:theme:api"))

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)

    testImplementation(project(":testing"))
    androidTestImplementation(project(":testing"))
}
