import org.gradle.api.Plugin
import org.gradle.api.Project
import soup.movie.buildlogic.configureAndroid
import soup.movie.buildlogic.configureKotlin

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }
            configureAndroid()
            configureKotlin()
        }
    }
}
