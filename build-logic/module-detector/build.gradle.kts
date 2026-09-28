plugins {
    `kotlin-dsl`
}

dependencies {
    testImplementation(libs.test.junit)
}

gradlePlugin {
    plugins {
        register("moduleDetector") {
            id = "moop.module.detector"
            implementationClass = "ModuleDetectorPlugin"
        }
    }
}
