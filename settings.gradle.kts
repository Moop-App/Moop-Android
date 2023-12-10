pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            content {
                includeGroupByRegex("com.android.*")
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("com.google.android.*")
                includeGroup("com.google.gms")
                includeGroup("com.google.firebase")
            }
        }
        maven {
            url = uri("https://jitpack.io")
            content {
                includeGroup("com.github.fornewid.metronome")
            }
        }
        maven {
            url = uri("https://devrepo.kakao.com/nexus/content/groups/public/")
            content {
                includeGroup("com.kakao.sdk")
            }
        }
        maven {
            url = uri("https://naver.jfrog.io/artifactory/maven/")
            content {
                includeGroup("com.naver.maps")
            }
        }

        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// Automatically detect modules.
ModuleDetector.modules(rootDir).forEach { module ->
    include(module)
}

private object ModuleDetector {

    fun modules(rootDir: File) : List<String> {
        return rootDir.listDirs().flatMap { dir ->
            findModules(parent = "", dir = dir)
        }
    }

    private fun findModules(parent: String, dir: File): List<String> {
        if (dir.isDirectory.not() || dir.isProject()) {
            return emptyList()
        }

        val current: String = parent + ":" + dir.name
        return if (dir.isModule()) {
            println("include '$current'")
            listOf(current)
        } else {
            dir.listDirs().flatMap { subDir ->
                findModules(parent = current, dir = subDir)
            }
        }
    }

    private fun File.isProject(): Boolean {
        if (isDirectory.not()) {
            return false
        }
        return listFiles().orEmpty().any {
            it.isFile && (it.name == "settings.gradle" || it.name == "settings.gradle.kts")
        }
    }

    private fun File.isModule(): Boolean {
        if (isDirectory.not()) {
            return false
        }
        return listFiles().orEmpty().any {
            it.isFile && (it.name == "build.gradle" || it.name == "build.gradle.kts")
        }
    }

    private fun File.listDirs(): List<File> {
        return listFiles().orEmpty().filter { it.isDirectory }
    }
}
