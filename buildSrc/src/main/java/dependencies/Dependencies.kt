package dependencies

object Versions {
    const val appId = "soup.movie"
    const val minSdk = 23
    const val targetSdk = 29
    const val compileSdk = 29
    const val buildTools = "29.0.3"
    const val versionCode = 104
    const val versionName = "1.0.4"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.2.1"

    // UI
    const val insetter = "dev.chrisbanes:insetter-ktx:0.3.1"
    const val lottie = "com.airbnb.android:lottie:3.5.0"
    const val recyclerViewAnimators = "jp.wasabeef:recyclerview-animators:4.0.1"
    const val stfalconImageviewer = "com.github.stfalcon:stfalcon-imageviewer:1.0.1"

    // Utils
    const val timber = "com.jakewharton.timber:timber:4.7.1"
    const val threetenAbp = "com.jakewharton.threetenabp:threetenabp:1.3.0"

    object Kotlin {
        private const val version = "1.4.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"
        const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:$version"
    }

    object Coroutines {
        private const val version = "1.4.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

    object Dagger {
        private const val version = "2.35"
        const val hilt = "com.google.dagger:hilt-android:$version"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$version"
        const val hiltTesting = "com.google.dagger:hilt-android-testing:$version"
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
    }

    object Google {
        const val gmsPlugin = "com.google.gms:google-services:4.3.4"
        const val material = "com.google.android.material:material:1.3.0-alpha03"
        const val play = "com.google.android.play:core:1.8.3"
    }

    object AndroidX {

        const val activity = "androidx.activity:activity-ktx:1.2.0-rc01"
        const val annotation = "androidx.annotation:annotation:1.2.0-beta01"
        const val appcompat = "androidx.appcompat:appcompat:1.3.0-beta01"
        const val browser = "androidx.browser:browser:1.3.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val core = "androidx.core:core-ktx:1.5.0-beta01"
        const val drawerlayout = "androidx.drawerlayout:drawerlayout:1.1.1"
        const val fragment = "androidx.fragment:fragment-ktx:1.3.0-rc02"
        const val preference = "androidx.preference:preference-ktx:1.1.1"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.2.0-beta01"
        const val startup = "androidx.startup:startup-runtime:1.0.0"
        const val transition = "androidx.transition:transition:1.4.0"
        const val viewpager2 = "androidx.viewpager2:viewpager2:1.1.0-alpha01"

        object Hilt {
            private const val version = "1.0.0-alpha03"
            const val work = "androidx.hilt:hilt-work:$version"
            const val viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:$version"
            const val compiler = "androidx.hilt:hilt-compiler:$version"
        }

        object Lifecycle {
            private const val version = "2.3.0-rc01"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val compiler = "androidx.lifecycle:lifecycle-common-java8:$version"
        }

        object Navigation {
            private const val version = "2.3.3"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val dynamicFeaturesFragment =
                "androidx.navigation:navigation-dynamic-features-fragment:$version"
            const val safeArgsPlugin =
                "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Room {
            private const val version = "2.3.0-beta01"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
        }

        object WorkManager {
            private const val version = "2.5.0"
            const val runtime = "androidx.work:work-runtime-ktx:$version"
            const val gcm = "androidx.work:work-gcm:$version"
            const val testing = "androidx.work:work-testing:$version"
        }
    }

    object Test {
        const val junit = "junit:junit:4.13"
        const val runner = "androidx.test:runner:1.1.0"
        const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val runtime = "com.squareup.retrofit2:retrofit:$version"
        const val serialization =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Glide {
        private const val version = "4.12.0"
        const val runtime = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Firebase {
        const val core = "com.google.firebase:firebase-core:17.2.1"
        const val crashlytics = "com.google.firebase:firebase-crashlytics:17.2.2"
        const val crashlyticsPlugin = "com.google.firebase:firebase-crashlytics-gradle:2.3.0"
        const val messaging = "com.google.firebase:firebase-messaging:20.0.1"
        const val dynamicLinks = "com.google.firebase:firebase-dynamic-links:19.0.0"
        const val ads = "com.google.firebase:firebase-ads:18.3.0"
    }
}
