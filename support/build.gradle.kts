dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("com.fasterxml.uuid:java-uuid-generator:4.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.0")
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}
