dependencies {
    api("org.springframework:spring-context:5.3.30")
    api("io.jsonwebtoken:jjwt:0.9.1")
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}
