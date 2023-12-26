plugins {
    id("moop.android.library")
    id("moop.android.compose")
    id("moop.android.hilt")
}

android {
    namespace = "soup.movie.feature.settings"
}

dependencies {
    implementation(project(":core:appupdate:api"))
    implementation(project(":core:kotlin"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:external"))
    implementation(project(":core:resources"))
    implementation(project(":data:settings:api"))
    implementation(project(":data:model"))
    implementation(project(":feature:theater"))
    implementation(project(":feature:theme:api"))

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.materialmotion.compose.core)

    testImplementation(project(":testing"))
    androidTestImplementation(project(":testing"))
}
