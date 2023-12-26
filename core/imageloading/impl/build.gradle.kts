plugins {
    id("moop.android.library")
    id("moop.android.hilt")
}

android {
    namespace = "soup.movie.core.imageloading.impl"
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        getByName("debug") {
            val filesAuthorityValue = "soup.movie.debug.shareprovider"
            buildConfigField("String", "FILES_AUTHORITY", "\"$filesAuthorityValue\"")
            manifestPlaceholders += mapOf("filesAuthority" to filesAuthorityValue)
        }
        getByName("release") {
            val filesAuthorityValue = "soup.movie.shareprovider"
            buildConfigField("String", "FILES_AUTHORITY", "\"$filesAuthorityValue\"")
            manifestPlaceholders += mapOf("filesAuthority" to filesAuthorityValue)
        }
    }
}

dependencies {
    implementation(project(":core:kotlin"))
    implementation(project(":core:imageloading:api"))
    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.startup)
    implementation(libs.coil.runtime)
}
