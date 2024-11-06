plugins {
    java
    id("pmd")
    id("checkstyle")
    id("org.sonarqube")
    kotlin("jvm") apply false
    id("org.jetbrains.kotlinx.kover")
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
    id("com.google.cloud.tools.jib") apply false
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx/kover/maven") }
    }
}

sonarqube {
    properties {
        val exclusions = mutableListOf<String>()
        file("${rootDir}/config/jacoco/exclude-coverage.txt").forEachLine {
            exclusions.add(it)
        }
        property("sonar.organization", "dailyge")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.token", System.getenv("SONAR_TOKEN"))
        property("sonar.projectKey", "dailyge_dailyge-server")
        property("sonar.language", "java")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.test.inclusions", "**/*Test.java,**/*Test.kt")
        property("sonar.scm.forceReloadAll", "true")
        property("sonar.exclusions", exclusions.joinToString(", "))
        property("sonar.coverage.jacoco.xmlReportPaths", "${buildDir}/reports/kover/report.xml")
        property(
            "sonar.java.pmd.reportPaths",
            layout.buildDirectory.file("reports/pmd/main.xml").get().asFile.toString()
        )
        property(
            "sonar.java.checkstyle.reportPaths",
            layout.buildDirectory.file("reports/checkstyle/main.xml").get().asFile.toString()
        )
    }
}

subprojects {
    apply(plugin = "pmd")
    apply(plugin = "java")
    apply(plugin = "checkstyle")
    apply(plugin = "application")
    apply(plugin = "java-library")
    apply(plugin = "org.sonarqube")
    apply(plugin = "java-test-fixtures")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "com.google.cloud.tools.jib")
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        }
    }

    dependencies {
        testImplementation("org.jetbrains.kotlin:kotlin-test")
        testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
        testImplementation("io.kotest:kotest-assertions-core:5.5.5")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.test {
        useJUnitPlatform()
        finalizedBy("koverXmlReport")
    }

    val exclusions = mutableListOf<String>()
    file("${rootDir}/config/jacoco/exclude-verification.txt").forEachLine {
        exclusions.add(it)
    }

    kover {
        reports {
            filters {
                excludes {
                    classes(*exclusions.toTypedArray())
                }
            }
        }
    }

    tasks.named<Checkstyle>("checkstyleMain") {
        dependsOn(tasks.named("compileTestJava"))
    }

    tasks.named<Pmd>("pmdMain") {
        dependsOn(tasks.named("compileTestJava"))
    }

    tasks.named("processResources") {
        mustRunAfter("compileJava")
    }

    sonarqube {
        properties {
            val javaMainDir = file("$buildDir/classes/java/main")
            val kotlinMainDir = file("$buildDir/classes/kotlin/main")
            property("sonar.java.binaries", listOf(javaMainDir, kotlinMainDir).filter { it.exists() }.joinToString(","))
            property("sonar.sources", listOf("src/main/java", "src/main/kotlin")
                .filter { file("$projectDir/$it").exists() }
                .joinToString(",")
            )
            property("sonar.tests", listOf("src/test/java", "src/test/kotlin")
                .filter { file("$projectDir/$it").exists() }
                .joinToString(",")
            )
            property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/kover/report.xml")
        }
    }

    checkstyle {
        toolVersion = (project.findProperty("checkStyleVersion") as String?).toString()
        configFile = file("${rootDir}/config/lint/dailyge-rule.xml")
        configProperties = mapOf("suppressionFile" to file("${rootDir}/config/lint/dailyge-suppressions.xml"))
        maxWarnings = 0
    }

    pmd {
        toolVersion = (project.findProperty("pmdVersion") as String?).toString()
        isConsoleOutput = true
        isIgnoreFailures = false
        ruleSetFiles = files("${rootDir}/config/lint/dailyge-pmd-rules.xml")
    }
}
