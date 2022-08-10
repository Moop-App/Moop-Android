import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.android.build.gradle.internal.dsl.DynamicFeatureExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import soup.movie.configureAndroidCompose

class AndroidDynamicFeatureComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.dynamic-feature")
            val extension = extensions.getByType<DynamicFeatureExtension>()
            configureAndroidCompose(extension)
        }
    }
}
