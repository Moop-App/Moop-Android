import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import soup.movie.buildlogic.configureAndroid
import soup.movie.buildlogic.configureKotlin
import soup.movie.buildlogic.implementation
import soup.movie.buildlogic.project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            configureAndroid()
            configureKotlin()

            dependencies {
                implementation(project(path = ":core:buildconfig"))
            }
        }
    }
}
