dependencies {
    api("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.1")
    implementation("com.github.luben:zstd-jni:1.5.2-4")
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}
