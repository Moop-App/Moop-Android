pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

include(":app")
include(":domain")
include(":testing")
include(":macrobenchmark")
include(":buildconfig")
include(":buildconfig-stub")

File("feature").listFiles()?.forEach {
    include(":feature:${it.name}")
}
File("data").listFiles()?.forEach {
    include(":data:${it.name}")
}
File("core").listFiles()?.forEach {
    include(":core:${it.name}")
}
