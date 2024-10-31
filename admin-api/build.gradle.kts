plugins {
    id("org.asciidoctor.jvm.convert")
    id("com.epages.restdocs-api-spec")
    id("org.liquibase.gradle")
}

configurations {
    create("asciidoctorExt")
}

dependencies {
    implementation(project(":modules:api"))
    implementation(project(":modules:auth"))
    implementation(project(":modules:security"))
    implementation(project(":storage:document"))
    implementation(project(":storage:rdb"))
    implementation(project(":storage:memory"))
    implementation(project(":support"))
    testImplementation(project(":storage:rdb"))
    testImplementation(project(":storage:document"))

    implementation("org.liquibase:liquibase-core")
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
