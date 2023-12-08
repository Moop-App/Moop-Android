import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import soup.movie.buildlogic.compileOnly
import soup.movie.buildlogic.configureAndroid
import soup.movie.buildlogic.configureKotlin
import soup.movie.buildlogic.project

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }
            configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                buildTypes {
                    release {
                        isMinifyEnabled = true
                        consumerProguardFiles("proguard-rules.pro")
                    }
                }
            }
            configureAndroid()
            configureKotlin()

            dependencies {
                compileOnly(project(path = ":core:buildconfig-stub"))
            }
        }
    }
}
