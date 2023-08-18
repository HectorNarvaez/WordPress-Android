import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy.eachDependency {
        when (requested.name) {
            "javapoet" -> useVersion("1.13.0")
        }
    }
}
