plugins {
    id("moop.android.library")
}

android {
    namespace = "soup.movie.domain"
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar.jdk)

    implementation(project(":core:kotlin"))
    implementation(project(":data:model"))

    implementation(libs.kotlin.stdlib)

    testImplementation(projects.testing)
}
