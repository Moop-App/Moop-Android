package soup.movie.buildlogic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)

            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",

                // https://github.com/Kotlin/kotlinx.serialization/issues/2145#issuecomment-1653091753
                "-Xstring-concat=inline",

                // https://youtrack.jetbrains.com/projects/KT/issues/KT-73255/Change-defaulting-rule-for-annotations
                "-Xannotation-default-target=param-property",
            )
        }
    }
}
