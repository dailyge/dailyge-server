plugins {
    id("org.liquibase.gradle")
    id("com.google.cloud.tools.jib")
    id("org.asciidoctor.jvm.convert")
    id("com.epages.restdocs-api-spec")
}

configurations {
    create("asciidoctorExt")
}

apply(from = "${rootDir}/jib/dailyge-api/jib.gradle")

dependencies {
    implementation(project(":modules:api"))
    implementation(project(":modules:auth"))
    implementation(project(":modules:security"))
    implementation(project(":storage:common"))
    implementation(project(":storage:rdb"))
    implementation(project(":storage:memory"))
    implementation(project(":storage:document"))
    implementation(project(":support"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation(platform("software.amazon.awssdk:bom:2.20.63"))
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:${property("awsSpringCloudVersion")}"))
    implementation("software.amazon.awssdk:sns")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")
    add(
        configurationName = "asciidoctorExt",
        dependencyNotation = "org.springframework.restdocs:spring-restdocs-asciidoctor"
    )
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

springBoot {
    mainClass.set("project.dailyge.app.DailygeApplication")
}

val snippetsDir = file("build/generated-snippets")
tasks.register("authOpenApiSpec") {
    doLast {
        val filePath = file("$buildDir/api-spec/openapi3.json")
        if (filePath.exists()) {
            try {
                val apiSpec = filePath.readText()
                val updatedApiSpec = addAuthOption(apiSpec)
                filePath.writeText(updatedApiSpec)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}

tasks.test {
    dependsOn("copySubmoduleConfig")
    mustRunAfter(":storage:rdb:build")
    outputs.dir(snippetsDir)
}

tasks.named("asciidoctor") {
    dependsOn(tasks.test)
    inputs.dir(snippetsDir)
    outputs.dir(file("$buildDir/docs/asciidoc"))
    finalizedBy("copyDocument")
}

tasks.register<Copy>("copyDocument") {
    mustRunAfter("asciidoctor")
    from(file("$buildDir/docs/asciidoc/"))
    into(file("$rootDir/dailyge-api/src/main/resources/static/docs"))
}

tasks.register<Copy>("copySubmoduleConfig") {
    from("$rootDir/config-module/dailyge-api")
    include("*.yml", "*.xml")
    into("$rootDir/dailyge-api/src/main/resources")
}

tasks.named("processResources") {
    dependsOn("copySubmoduleConfig")
}

tasks.withType<JavaCompile>().configureEach {
    outputs.cacheIf { true }
}

tasks.named("jib") {
    dependsOn("copySubmoduleConfig", "openapi3", "copyDocument")
    finalizedBy("authOpenApiSpec")
    enabled = true
}

tasks.build {
    dependsOn("asciidoctor")
}

openapi3 {
    setServer("https://api-dev.dailyge.com")
    title = "Dailyge Api"
    description = "Dailyge Api 문서 입니다."
    version = "1.0.0"
    format = "json"
    outputDirectory = "$buildDir/api-spec"
}
