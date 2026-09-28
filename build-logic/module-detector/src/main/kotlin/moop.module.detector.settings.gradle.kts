// Includes every directory that has a build.gradle(.kts) file as a module.
// Hidden and build directories are skipped, and so are nested builds with their own settings file.

fun File.subDirs(): List<File> =
    listFiles().orEmpty().filter { !it.name.startsWith(".") && it.name != "build" && it.isDirectory }

fun findModules(dir: File, path: String): List<String> = when {
    listOf("settings.gradle", "settings.gradle.kts").any { dir.resolve(it).isFile } -> emptyList()
    listOf("build.gradle", "build.gradle.kts").any { dir.resolve(it).isFile } -> listOf(path)
    else -> dir.subDirs().flatMap { findModules(it, "$path:${it.name}") }
}

include(rootDir.subDirs().flatMap { findModules(it, ":${it.name}") }.sorted())
