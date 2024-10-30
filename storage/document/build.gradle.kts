plugins {
    id("java-test-fixtures")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("com.fasterxml.uuid:java-uuid-generator:4.0")
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}
