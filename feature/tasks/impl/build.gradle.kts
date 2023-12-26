plugins {
    id("moop.android.library")
    id("moop.android.hilt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "soup.movie.feature.tasks.impl"
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar.jdk)

    implementation(project(":core:kotlin"))
    implementation(project(":core:logger"))
    implementation(project(":core:resources"))
    implementation(project(":data:repository:api"))
    implementation(project(":data:model"))
    implementation(project(":domain"))
    implementation(project(":feature:tasks:api"))
    implementation(project(":feature:notification:api"))

    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.androidx.work.runtime)
    androidTestImplementation(libs.androidx.work.testing)

    testImplementation(project(":testing"))
    androidTestImplementation(project(":testing"))
}
