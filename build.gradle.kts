plugins {
    java
    id("pmd")
    id("jacoco")
    id("checkstyle")
    id("org.sonarqube")
    kotlin("jvm") apply false
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
        property("sonar.test.inclusions", "**/*Test.java")
        property("sonar.scm.forceReloadAll", "true")
        property("sonar.exclusions", exclusions.joinToString(", "))
        property("sonar.java.coveragePlugin", "jacoco")
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
    apply(plugin = "jacoco")
    apply(plugin = "checkstyle")
    apply(plugin = "application")
    apply(plugin = "java-library")
    apply(plugin = "org.sonarqube")
    apply(plugin = "java-test-fixtures")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "com.google.cloud.tools.jib")
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        }
    }

    dependencies {
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<JacocoReport> {
        dependsOn(tasks.test)
        reports {
            xml.required.set(true)
            html.required.set(false)
            csv.required.set(false)
            xml.outputLocation.set(file("$buildDir/reports/jacoco/test/jacocoTestReport.xml"))
        }

        val exclusions = extractQClass().toMutableList()
        file("${rootDir}/config/jacoco/exclude-coverage.txt").forEachLine {
            exclusions.add(it)
        }

        classDirectories.from(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude(exclusions)
                }
            })
        )
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

    tasks.test {
        useJUnitPlatform()
        finalizedBy(tasks.jacocoTestReport)
        jvmArgs("--add-opens=java.base/java.time=ALL-UNNAMED")
    }

    sonarqube {
        properties {
            property("sonar.java.binaries", "$buildDir/classes/java/main")
            if (file("${projectDir}/src/main/java").exists()) {
                property("sonar.sources", "src/main/java")
            }
            if (file("${projectDir}/src/test/java").exists()) {
                property("sonar.tests", "src/test/java")
            }
            property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")
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

fun extractQClass(): List<String> {
    val qClasses = mutableListOf<String>()
    for (qPattern in 'A'..'Z') {
        qClasses.add("**/*Q$qPattern*")
    }
    return qClasses
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-proc:none")
}
