plugins {
    id("org.asciidoctor.jvm.convert")
    id("com.epages.restdocs-api-spec")
    id("org.liquibase.gradle")
}

configurations {
    create("asciidoctorExt")
}

dependencies {
    implementation(project(":storage:document"))
    implementation(project(":storage:rdb"))
    implementation(project(":storage:memory"))
    implementation(project(":support"))

    testImplementation(project(":storage:rdb"))
    testImplementation(project(":storage:document"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")
    implementation("org.liquibase:liquibase-core")

    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:${property("restdocsApiSpecVersion")}")
    testImplementation("com.epages:restdocs-api-spec-restassured:${property("restdocsApiSpecVersion")}")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")

    "asciidoctorExt"("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

springBoot {
    mainClass.set("project.dailyge.app.DailygeAdminApplication")
}

val snippetsDir = file("build/generated-snippets")

tasks.test {
    dependsOn("copySubmoduleConfig")
    outputs.dir(snippetsDir)
    dependsOn(":storage:rdb:test")
}

tasks.asciidoctor {
    attributes(mapOf("snippets" to snippetsDir))
    inputs.dir(snippetsDir)
    dependsOn(tasks.test)
    onlyIf { tasks.test.get().state.executed && tasks.test.get().state.failure == null }
}

tasks.build {
    dependsOn(tasks.named("copyDocument"))
    dependsOn(":admin-api:openapi3")
}

tasks.bootJar {
    dependsOn(tasks.asciidoctor)
    from("${tasks.asciidoctor.get().outputDir}/html5") {
        into("static/docs")
    }
}

tasks.register<Copy>("copyDocument") {
    dependsOn(tasks.asciidoctor)
    from(file("build/docs/asciidoc"))
    into(file("src/main/resources/static/docs"))
    onlyIf { tasks.test.get().state.executed && tasks.test.get().state.failure == null }
}

tasks.register<Copy>("copySubmoduleConfig") {
    from("${rootDir}/config-module/admin-api")
    include("*.yml")
    into("${rootDir}/admin-api/src/main/resources")
}

tasks.named("processResources") {
    dependsOn(":admin-api:copySubmoduleConfig")
    dependsOn(":storage:rdb:compileTestJava")
}

tasks.withType<JavaCompile>().configureEach {
    outputs.cacheIf { true }
}

openapi3 {
    setServer("https://api-dev.dailyge.com")
    title = "Dailyge Admin Api"
    description = "Dailyge Admin Api 문서 입니다."
    version = "1.0.0"
    format = "json"
}
