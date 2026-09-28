import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ModuleDetectorPluginTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    @Test
    fun findModules_includesDirectoriesWithBuildFile() {
        createFile("build.gradle.kts")
        createFile("app/build.gradle")
        createFile("feature/home/api/build.gradle.kts")
        createFile("feature/home/impl/build.gradle")
        createFile("feature/home/impl/src/build.gradle")
        tempFolder.newFolder("docs")

        assertEquals(listOf(":app", ":feature:home:api", ":feature:home:impl"), findModules(tempFolder.root))
    }

    @Test
    fun findModules_skipsHiddenAndBuildDirectories() {
        createFile(".gradle/cache/build.gradle")
        createFile("build/generated/build.gradle")
        createFile("feature/build/tmp/build.gradle")
        createFile("app/build.gradle")

        assertEquals(listOf(":app"), findModules(tempFolder.root))
    }

    @Test
    fun findModules_skipsNestedBuilds() {
        createFile("build-logic/settings.gradle.kts")
        createFile("build-logic/convention/build.gradle.kts")
        createFile("app/build.gradle")

        assertEquals(listOf(":app"), findModules(tempFolder.root))
    }

    private fun createFile(path: String) {
        tempFolder.root.resolve(path).apply { parentFile.mkdirs() }.createNewFile()
    }
}
