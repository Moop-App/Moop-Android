import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import java.io.File

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
        val baseline = root.tasks.register<ModuleRulesTask>("moduleRulesBaseline") { update = true }
        root.tasks.register<ModuleRulesTask>("moduleRules") { mustRunAfter(baseline) }
        root.tasks.withType<ModuleRulesTask>().configureEach {
            group = "verification"
            // Depending on the tasks by path makes configure-on-demand configure every module.
            dependsOn(modules.map { "${it.path}:moduleRulesInfo" })
            violationFiles.from(infoFiles)
        }
    }
}

/** Module paths from the declaration buckets; AGP copies them, and the module itself, into classpaths. */
private fun Project.declaredDependencies(): Set<String> = configurations
    .filter { !it.isCanBeResolved && !it.isCanBeConsumed }
    .flatMap { it.dependencies.withType(ProjectDependency::class.java) }
    .map { it.path }
    .toSet()

/** Writes the violations of one module for the root tasks to collect. */
abstract class ModuleRulesInfoTask : DefaultTask() {
    @get:Input
    abstract val violations: ListProperty<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun write() = outputFile.get().asFile.writeText(violations.get().joinToString("") { "$it\n" })
}

/** Writes ([update]) or checks module-rules.txt. */
abstract class ModuleRulesTask : DefaultTask() {
    @get:InputFiles
    abstract val violationFiles: ConfigurableFileCollection

    @get:Input
    var update = false

    @get:Internal
    val baselineFile: File = project.file("module-rules.txt")

    @TaskAction
    fun run() {
        val actual = violationFiles.flatMap { it.readLines() }.sorted()
        if (update) {
            baselineFile.writeText(actual.joinToString("") { "$it\n" })
            return
        }
        val diff = baselineDiff(expected = baselineFile.takeIf { it.exists() }?.readLines().orEmpty(), actual = actual)
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
    if (hasHilt && !isApp && !path.isImpl()) add("hilt: $path")
    if (!isApp) dependencies.filter { it.isImpl() }.forEach { add("impl: $path -> $it") }
    dependencies.filter { path.layer() >= 0 && it.layer() > path.layer() }.forEach { add("layer: $path -> $it") }
}

internal fun baselineDiff(expected: List<String>, actual: List<String>): List<String> =
    (expected - actual.toSet()).map { "- $it" } + (actual - expected.toSet()).map { "+ $it" }

private val LAYERS = listOf("core", "data", "feature", "app")

private fun String.layer() = LAYERS.indexOf(split(':')[1])

private fun String.isImpl() = endsWith(":impl")
