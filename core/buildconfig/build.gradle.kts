import java.util.Properties

plugins {
    id("moop.android.buildconfig")
}

android {
    namespace = "soup.movie.buildconfig"

    defaultConfig {
        val properties = Properties()
        val keyFile = rootProject.file("signing/key.properties")
        if (keyFile.exists()) {
            keyFile.inputStream().use { properties.load(it) }
        }

        buildConfigField("int", "VERSION_CODE", rootProject.extra["versionCode"].toString())
        buildConfigField("String", "VERSION_NAME", "\"" + rootProject.extra["versionName"].toString() + "\"")
        buildConfigField("String", "API_BASE_URL", "\"" + properties.getProperty("MOOP_API_BASE_URL", "https://moop-public-api.firebaseio.com/alpha/") + "\"")
    }
    buildFeatures {
        buildConfig = true
    }
}
