dependencies {
    api("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}
