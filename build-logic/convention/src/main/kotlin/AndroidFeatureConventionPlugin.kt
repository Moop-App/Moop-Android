import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import soup.movie.buildlogic.implementation
import soup.movie.buildlogic.project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("moop.android.library")

            dependencies {
                implementation(project(path = ":data:repository:api"))
            }
        }
    }
}
