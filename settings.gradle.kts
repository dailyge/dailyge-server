pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.jvm") version extra["kotlinVersion"] as String
        id("org.jetbrains.kotlin.plugin.allopen") version extra["kotlinVersion"] as String
        id("org.jetbrains.kotlin.plugin.noarg") version extra["kotlinVersion"] as String
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/plugin-release") }
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.springframework.boot" -> useVersion(extra["springBootVersion"] as String)
                "io.spring.dependency-management" -> useVersion(extra["springDependencyManagementVersion"] as String)
                "org.testcontainers" -> useVersion(extra["testcontainersVersion"] as String)
                "com.ewerk.gradle.plugins.querydsl" -> useVersion(extra["ewerkVersion"] as String)
                "org.asciidoctor.jvm.convert" -> useVersion(extra["jvmConvertVersion"] as String)
                "com.google.cloud.tools.jib" -> useVersion(extra["jibVersion"] as String)
                "org.sonarqube" -> useVersion(extra["sonarQubeVersion"] as String)
                "com.epages.restdocs-api-spec" -> useVersion(extra["restdocsApiSpecVersion"] as String)
                "com.epages:restdocs-api-spec-mockmvc" -> useVersion(extra["restdocsApiSpecVersion"] as String)
                "org.liquibase.gradle" -> useVersion(extra["liquibaseVersion"] as String)
                "org.liquibase:liquibase-core" -> useVersion(extra["liquibaseCoreVersion"] as String)
            }
        }
    }
}

gradle.projectsLoaded {
    gradle.startParameter.maxWorkerCount = Runtime.getRuntime()
        .availableProcessors().div(2)
        .coerceAtLeast(1)
}

buildCache {
    local {
        isEnabled = true
        directory = file("${rootProject.projectDir}/.gradle/build-cache")
    }
}

rootProject.name = "dailyge"
include(
    "admin-api",
    "dailyge-api",
    "modules:auth",
    "modules:security",
    "modules:api",
    "storage:common",
    "storage:document",
    "storage:rdb",
    "storage:memory",
    "support"
)
