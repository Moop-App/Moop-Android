plugins {
    `kotlin-dsl`
}

dependencies {
    testImplementation(libs.test.junit)
}

gradlePlugin {
    plugins {
        register("moduleRules") {
            id = "moop.module.rules"
            implementationClass = "ModuleRulesPlugin"
        }
    }
}
