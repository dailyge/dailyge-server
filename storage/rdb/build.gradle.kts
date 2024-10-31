plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("org.jetbrains.kotlin.plugin.allopen")
    id("org.springframework.boot") apply false
    id("org.asciidoctor.jvm.convert") apply false
}

dependencies {
    // JPA
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("com.querydsl:querydsl-jpa:${property("queryDslVersion")}:jakarta")

    // MySQL
    runtimeOnly("mysql:mysql-connector-java:${property("mysqlVersion")}")

    // AnnotationProcessor
    kapt("com.querydsl:querydsl-apt:${property("queryDslVersion")}:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("org.springframework.context.annotation.Component")
    annotation("org.springframework.context.annotation.Configuration")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
    annotation("kotlin.Metadata")
}

noArg {
    annotation("jakarta.persistence.Entity")
}

val querydslDir = "src/main/generated"

sourceSets {
    main {
        java.srcDir(querydslDir)
    }
}

tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory.set(file(querydslDir))
}

tasks.clean {
    doLast {
        file(querydslDir).deleteRecursively()
    }
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}
