import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.work.DisableCachingByDefault

/**
 * Records module rule violations in module-rules.txt:
 * `moduleRulesBaseline` writes the file and `moduleRules` fails when it is out of date.
 */
class ModuleRulesPlugin : Plugin<Project> {
    override fun apply(root: Project) {
        val modules = root.subprojects.filter { it.buildFile.exists() }
        val infoFiles = modules.map { module ->
            module.tasks.register<ModuleRulesInfoTask>("moduleRulesInfo") {
                violations.set(
                    module.provider {
                        findViolations(
                            path = module.path,
                            hasHilt = module.pluginManager.hasPlugin("dagger.hilt.android.plugin"),
                            dependencies = module.declaredDependencies(),
                        )
                    },
                )
                outputFile.set(module.layout.buildDirectory.file("module-rules.txt"))
            }.flatMap { it.outputFile }
        }
        root.tasks.register<ModuleRulesTask>("moduleRulesBaseline") {
            description = "Writes the module rule violations to module-rules.txt."
            update.set(true)
        }
        root.tasks.register<ModuleRulesTask>("moduleRules") {
            description = "Fails when the module rule violations differ from module-rules.txt."
            update.set(false)
        }
        root.tasks.withType<ModuleRulesTask>().configureEach {
            group = "verification"
            // Depending on the tasks by path makes configure-on-demand configure every module.
            dependsOn(modules.map { "${it.path}:moduleRulesInfo" })
            violationFiles.from(infoFiles)
            baselineFile.set(root.layout.projectDirectory.file("module-rules.txt"))
        }
    }
}

/** Module paths from the declaration buckets; AGP copies them, and the module itself, into classpaths. */
private fun Project.declaredDependencies(): Set<String> = configurations
    .filter { !it.isCanBeResolved && !it.isCanBeConsumed }
    .flatMap { it.dependencies.withType(ProjectDependency::class.java) }
    .map { it.path }
    .toSet()

@DisableCachingByDefault(because = "Writes a few lines computed from its inputs")
abstract class ModuleRulesInfoTask : DefaultTask() {
    @get:Input
    abstract val violations: ListProperty<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun write() = outputFile.get().asFile.writeText(violations.get().joinToString("") { "$it\n" })
}

@UntrackedTask(because = "Checks or rewrites module-rules.txt in the source tree")
abstract class ModuleRulesTask : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NONE)
    abstract val violationFiles: ConfigurableFileCollection

    @get:Input
    abstract val update: Property<Boolean>

    @get:Internal
    abstract val baselineFile: RegularFileProperty

    @TaskAction
    fun run() {
        val actual = violationFiles.flatMap { it.readLines() }.sorted()
        val file = baselineFile.get().asFile
        if (update.get()) {
            file.writeText(actual.joinToString("") { "$it\n" })
            return
        }
        val diff = baselineDiff(expected = file.readLines(), actual = actual)
        if (diff.isNotEmpty()) {
            throw GradleException(
                "Module rules changed in module-rules.txt:\n" + diff.joinToString("\n") +
                    "\n\nIf this is intended, re-baseline with ./gradlew moduleRulesBaseline",
            )
        }
    }
}

/**
 * The rule violations of the module at [path], one line each: Hilt outside the app and impl modules,
 * an impl module dependency outside the app, and a dependency on a higher layer.
 */
internal fun findViolations(path: String, hasHilt: Boolean, dependencies: Set<String>): List<String> = buildList {
    val isApp = path == ":app"
    val layer = path.layer()
    if (hasHilt && !isApp && !path.isImpl()) add("hilt: $path")
    if (!isApp) dependencies.filter { it.isImpl() }.forEach { add("impl: $path -> $it") }
    if (layer >= 0) dependencies.filter { it.layer() > layer }.forEach { add("layer: $path -> $it") }
}

internal fun baselineDiff(expected: List<String>, actual: List<String>): List<String> =
    (expected - actual.toSet()).map { "- $it" } + (actual - expected.toSet()).map { "+ $it" }

private val LAYERS = listOf("core", "data", "feature", "app")

private fun String.layer() = LAYERS.indexOf(split(':')[1])

private fun String.isImpl() = endsWith(":impl")
