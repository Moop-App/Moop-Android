plugins {
    id("moop.android.library")
}

android {
    namespace = "soup.movie.log"
}

dependencies {
    implementation(libs.timber)
}
