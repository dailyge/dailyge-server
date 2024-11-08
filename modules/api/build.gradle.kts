dependencies {
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    api("javax.xml.bind:jaxb-api:2.3.1")

    api("com.google.code.gson:gson:2.10")

    api("org.springframework.restdocs:spring-restdocs-restassured")
    api("org.springframework.restdocs:spring-restdocs-mockmvc")
    api("com.epages:restdocs-api-spec-mockmvc:${property("restdocsApiSpecVersion")}")
    api("com.epages:restdocs-api-spec-restassured:${property("restdocsApiSpecVersion")}")
    api("org.testcontainers:testcontainers")
    api("org.testcontainers:localstack")
    api("org.testcontainers:junit-jupiter")
    api("org.testcontainers:mysql")
    api("org.springframework.cloud:spring-cloud-contract-wiremock")
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}
