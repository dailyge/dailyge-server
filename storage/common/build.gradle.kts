dependencies {
    api("org.redisson:redisson-spring-boot-starter:3.16.5")
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}
