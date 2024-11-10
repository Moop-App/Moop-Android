package soup.movie.buildlogic

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

fun Project.configureCompose() {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    android {
        buildFeatures.compose = true

        dependencies {
            val bom = libs.findLibrary("compose-bom").get()
            implementation(platform(bom))
            androidTestImplementation(platform(bom))

            implementation(libs.findLibrary("compose-ui-tooling-preview").get())
            debugImplementation(libs.findLibrary("compose-ui-tooling").get())
        }
    }
}

private fun Project.android(action: BaseExtension.() -> Unit) {
    extensions.configure(action)
}
