package dependencies

object Versions {
    const val appId = "soup.movie"
    const val minSdk = 23
    const val targetSdk = 30
    const val compileSdk = 30
    const val buildTools = "30.0.2"
    const val versionCode = 106
    const val versionName = "1.0.6"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.2"

    // UI
    const val insetter = "dev.chrisbanes.insetter:insetter:0.6.0"
    const val photoview = "com.github.chrisbanes:PhotoView:2.3.0"
    const val recyclerViewAnimators = "jp.wasabeef:recyclerview-animators:4.0.2"
    const val kakaoLink = "com.kakao.sdk:v2-link:2.7.0"
    const val naverMap = "com.naver.maps:map-sdk:3.12.0"
    const val ticker = "com.robinhood.ticker:ticker:2.0.2"
    const val playServicesLocation = "com.google.android.gms:play-services-location:17.1.0"

    // Utils
    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val threetenAbp = "com.jakewharton.threetenabp:threetenabp:1.3.1"

    object Kotlin {
        private const val version = "1.5.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2"
        const val serializationPlugin = "org.jetbrains.kotlin:kotlin-serialization:$version"
    }

    object Coroutines {
        private const val version = "1.5.1"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Dagger {
        private const val version = "2.38.1"
        const val hilt = "com.google.dagger:hilt-android:$version"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$version"
        const val hiltTesting = "com.google.dagger:hilt-android-testing:$version"
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
    }

    object Google {
        const val gmsPlugin = "com.google.gms:google-services:4.3.8"
        const val play = "com.google.android.play:core:1.10.0"
    }

    object AndroidX {
        const val activity = "androidx.activity:activity-ktx:1.3.1"
        const val annotation = "androidx.annotation:annotation:1.2.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.1"
        const val browser = "androidx.browser:browser:1.3.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.0"
        const val core = "androidx.core:core-ktx:1.6.0"
        const val drawerlayout = "androidx.drawerlayout:drawerlayout:1.1.1"
        const val fragment = "androidx.fragment:fragment-ktx:1.3.6"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.2.1"
        const val startup = "androidx.startup:startup-runtime:1.1.0"
        const val transition = "androidx.transition:transition:1.4.1"
        const val viewpager2 = "androidx.viewpager2:viewpager2:1.1.0-beta01"

        object Hilt {
            private const val version = "1.0.0"
            const val work = "androidx.hilt:hilt-work:$version"
            const val viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:$version"
            const val compiler = "androidx.hilt:hilt-compiler:$version"
        }

        object Lifecycle {
            private const val version = "2.3.1"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val process = "androidx.lifecycle:lifecycle-process:$version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val compiler = "androidx.lifecycle:lifecycle-common-java8:$version"
        }

        object Navigation {
            private const val version = "2.3.5"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val dynamicFeaturesFragment =
                "androidx.navigation:navigation-dynamic-features-fragment:$version"
            const val safeArgsPlugin =
                "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Room {
            private const val version = "2.3.0"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
        }

        object WorkManager {
            private const val version = "2.6.0"
            const val runtime = "androidx.work:work-runtime-ktx:$version"
            const val gcm = "androidx.work:work-gcm:$version"
            const val testing = "androidx.work:work-testing:$version"
        }

        object DataStore {
            private const val version = "1.0.0"
            const val preferences = "androidx.datastore:datastore-preferences:$version"
        }

        object Test {
            const val core = "androidx.test:core-ktx:1.4.0"
            const val junit = "androidx.test.ext:junit-ktx:1.1.3"
            const val truth = "androidx.test.ext:truth:1.4.0"
            const val runner = "androidx.test:runner:1.4.0"
            const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        }
    }

    object Material {
        const val view = "com.google.android.material:material:1.4.0"
        const val composeThemeAdapter = "com.google.android.material:compose-theme-adapter:1.0.2"
    }

    object Compose {
        const val version = "1.0.2"
        const val activity = "androidx.activity:activity-compose:1.3.1"
        const val constraintlayout =
            "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha07"
        const val navigation = "androidx.navigation:navigation-compose:2.4.0-alpha06"
        const val runtime = "androidx.compose.runtime:runtime:$version"
        const val runtime_livedata = "androidx.compose.runtime:runtime-livedata:$version"
        const val foundation = "androidx.compose.foundation:foundation:$version"
        const val foundation_layout = "androidx.compose.foundation:foundation-layout:$version"
        const val ui = "androidx.compose.ui:ui:$version"
        const val ui_test_junit4 = "androidx.compose.ui:ui-test-junit4:$version"
        const val ui_test_manifest = "androidx.compose.ui:ui-test-manifest:$version"
        const val ui_tooling = "androidx.compose.ui:ui-tooling:$version"
        const val ui_util = "androidx.compose.ui:ui-util:$version"
        const val material = "androidx.compose.material:material:$version"
        const val material_icons_extended =
            "androidx.compose.material:material-icons-extended:$version"
        const val animation = "androidx.compose.animation:animation:$version"
    }

    object Accompanist {
        private const val version = "0.18.0"
        const val drawablepainter = "com.google.accompanist:accompanist-drawablepainter:$version"
        const val flowlayout = "com.google.accompanist:accompanist-flowlayout:$version"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val insets_ui = "com.google.accompanist:accompanist-insets-ui:$version"
        const val pager = "com.google.accompanist:accompanist-pager:$version"
        const val pager_indicators = "com.google.accompanist:accompanist-pager-indicators:$version"
        const val permissions = "com.google.accompanist:accompanist-permissions:$version"
        const val placeholder = "com.google.accompanist:accompanist-placeholder-material:$version"
        const val swiperefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
        const val systemuicontroller =
            "com.google.accompanist:accompanist-systemuicontroller:$version"
    }

    object ComposeExperimental {
        private const val version = "0.1.3"
        const val material = "com.github.fornewid.compose-experimental:material:$version"
        const val ui = "com.github.fornewid.compose-experimental:ui:$version"
    }

    object Test {
        const val junit = "junit:junit:4.13"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val runtime = "com.squareup.retrofit2:retrofit:$version"
        const val serialization =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Coil {
        private const val version = "1.3.2"
        const val runtime = "io.coil-kt:coil:$version"
        const val compose = "io.coil-kt:coil-compose:$version"
    }

    object Lottie {
        private const val version = "4.1.0"
        const val compose = "com.airbnb.android:lottie-compose:$version"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:28.4.0"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val config = "com.google.firebase:firebase-config-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val crashlyticsPlugin = "com.google.firebase:firebase-crashlytics-gradle:2.7.1"
        const val messaging = "com.google.firebase:firebase-messaging-ktx"
        const val dynamicLinks = "com.google.firebase:firebase-dynamic-links-ktx"
        const val perf = "com.google.firebase:firebase-perf-ktx"
        const val perfPlugin = "com.google.firebase:perf-plugin:1.4.0"
        const val ads = "com.google.android.gms:play-services-ads:20.3.0"
    }
}
