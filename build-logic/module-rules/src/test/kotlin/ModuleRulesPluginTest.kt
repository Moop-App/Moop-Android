import org.junit.Assert.assertEquals
import org.junit.Test

class ModuleRulesPluginTest {

    @Test
    fun findViolations_reportsHiltOutsideAppAndImplModules() {
        assertEquals(listOf("hilt: :core:kotlin"), findViolations(":core:kotlin", hasHilt = true, dependencies = emptySet()))
        assertEquals(emptyList<String>(), findViolations(":app", hasHilt = true, dependencies = emptySet()))
        assertEquals(emptyList<String>(), findViolations(":feature:home:impl", hasHilt = true, dependencies = emptySet()))
    }

    @Test
    fun findViolations_reportsImplDependencyOutsideApp() {
        assertEquals(
            listOf("impl: :feature:home:api -> :feature:home:impl"),
            findViolations(":feature:home:api", hasHilt = false, dependencies = setOf(":feature:home:impl")),
        )
        assertEquals(emptyList<String>(), findViolations(":app", hasHilt = false, dependencies = setOf(":feature:home:impl")))
    }

    @Test
    fun findViolations_reportsDependencyOnHigherLayer() {
        assertEquals(
            listOf("layer: :core:datetime -> :data:model"),
            findViolations(":core:datetime", hasHilt = false, dependencies = setOf(":data:model", ":core:kotlin", ":testing")),
        )
        assertEquals(emptyList<String>(), findViolations(":testing", hasHilt = false, dependencies = setOf(":feature:home:api")))
    }

    @Test
    fun baselineDiff_listsRemovedThenAddedLines() {
        assertEquals(
            listOf("- hilt: :a", "+ hilt: :c"),
            baselineDiff(expected = listOf("hilt: :a", "hilt: :b"), actual = listOf("hilt: :b", "hilt: :c")),
        )
    }
}
