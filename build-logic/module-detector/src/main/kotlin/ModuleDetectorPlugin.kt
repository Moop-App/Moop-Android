import org.gradle.api.Plugin
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.initialization.Settings
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import java.io.File

/**
 * Includes every directory that has a build.gradle(.kts) file as a module,
 * so settings.gradle.kts needs no include list.
 */
class ModuleDetectorPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        val modules = settings.providers.of(ModulesValueSource::class.java) {
            parameters.rootDir.set(settings.rootDir)
        }
        settings.include(modules.get())
    }
}

/**
 * Makes the configuration cache compare only the detected modules,
 * not every directory listed while detecting them.
 */
abstract class ModulesValueSource : ValueSource<List<String>, ModulesValueSource.Parameters> {
    interface Parameters : ValueSourceParameters {
        val rootDir: DirectoryProperty
    }

    override fun obtain(): List<String> = findModules(parameters.rootDir.get().asFile)
}

/**
 * Returns the sorted Gradle paths of the modules below [rootDir].
 * Hidden and build directories are skipped, and so are nested builds with their own settings file.
 */
internal fun findModules(rootDir: File): List<String> =
    rootDir.subDirs().flatMap { it.modules(":${it.name}") }.sorted()

private fun File.modules(path: String): List<String> = when {
    hasFile("settings.gradle", "settings.gradle.kts") -> emptyList()
    hasFile("build.gradle", "build.gradle.kts") -> listOf(path)
    else -> subDirs().flatMap { it.modules("$path:${it.name}") }
}

private fun File.hasFile(vararg names: String): Boolean = names.any { resolve(it).isFile }

private fun File.subDirs(): List<File> =
    listFiles().orEmpty().filter { !it.name.startsWith(".") && it.name != "build" && it.isDirectory }
