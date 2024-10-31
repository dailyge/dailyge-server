dependencies{
    api("org.liquibase:liquibase-core")
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}
